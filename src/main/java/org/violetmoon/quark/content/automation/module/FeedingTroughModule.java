package org.violetmoon.quark.content.automation.module;

import java.util.Optional;
import java.util.WeakHashMap;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.content.automation.block.FeedingTroughBlock;
import org.violetmoon.quark.content.automation.block.be.FeedingTroughBlockEntity;
import org.violetmoon.quark.mixin.mixins.accessor.AccessorTemptingSensor;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.ZEntityJoinLevel;
import org.violetmoon.zeta.event.play.entity.living.ZBabyEntitySpawn;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

/**
 * @author WireSegal
 *         Created at 9:48 AM on 9/20/19.
 */
@ZetaLoadModule(category = "automation")
public class FeedingTroughModule extends ZetaModule {

	//using a ResourceKey because they're interned, and Holder.Reference#is leverages this for a very efficient implementation
	private static final ResourceKey<PoiType> FEEDING_TROUGH_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Quark.asResource("feeding_trough"));

	public static BlockEntityType<FeedingTroughBlockEntity> blockEntityType;
	@Hint
	Block feeding_trough;

	private static final String TAG_CACHE = "quark:feedingTroughCache";

	@Config(description = "How long, in game ticks, between animals being able to eat from the trough")
	@Config.Min(1)
	public static int cooldown = 30;

	@Config(description = "The maximum amount of animals allowed around the trough's range for an animal to enter love mode")
	public static int maxAnimals = 32;

	@Config(description = "The chance (between 0 and 1) for an animal to enter love mode when eating from the trough")
	@Config.Min(value = 0.0, exclusive = true)
	@Config.Max(1.0)
	public static double loveChance = 0.333333333;

	@Config
	public static double range = 10;

	@Config(description = "Chance that an animal decides to look for a through. Closer it is to 1 the more performance it will take. Decreasing will make animals take longer to find one")
	public static double lookChance = 0.005;

	private static final WeakHashMap<Entity, TroughPointer> NEARBY_TROUGH_CACHE = new WeakHashMap<>();

	private static final ThreadLocal<Boolean> breedingOccurred = ThreadLocal.withInitial(() -> false);

	@PlayEvent
	public void onBreed(ZBabyEntitySpawn.Lowest event) {
		if(event.getCausedByPlayer() == null && event.getParentA().level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
			breedingOccurred.set(true);
	}

	@PlayEvent
	public void onOrbSpawn(ZEntityJoinLevel event) {
		if(event.getEntity() instanceof ExperienceOrb && breedingOccurred.get()) {
			event.setCanceled(true);
			breedingOccurred.remove();
		}
	}

	// Both TempingSensor and TemptGoal work by keeping track of a nearby player who is holding food.
	// The Feeding Trough causes mobs to pathfind to it by injecting a fakeplayer into these AI goals, who stands at the
	// location of the Trough and holds food they like.

	// The "realPlayer" parameter represents a real player located by existing TemptingSensor/TemptGoal code.
	// If there is a real player, and they are holding food, we don't swap them for a fakeplayer, so that animals path to
	// real players before they consider pathing to the Trough.

	public static @Nullable Player modifyTemptingSensor(@Nullable Player realPlayer, TemptingSensor sensor, Animal animal, ServerLevel level) {
		return modifyTempt(realPlayer, level, animal, ((AccessorTemptingSensor) sensor).quark$getTemptations());
	}

	public static @Nullable Player modifyTemptGoal(@Nullable Player realPlayer, TemptGoal goal, Animal animal, ServerLevel level) {
		return modifyTempt(realPlayer, level, animal, goal.items);
	}

	private static @Nullable Player modifyTempt(@Nullable Player realPlayer, ServerLevel level, Animal animal, Ingredient temptations) {
		//early-exit conditions
		if(!Quark.ZETA.modules.isEnabled(FeedingTroughModule.class) ||
			!animal.canFallInLove() ||
			animal.getAge() != 0
		) {
			return realPlayer;
		}

		//deference to real players
		if(realPlayer != null && (temptations.test(realPlayer.getMainHandItem()) || temptations.test(realPlayer.getOffhandItem())))
			return realPlayer;

		//do we already know about a nearby trough?
		TroughPointer pointer = NEARBY_TROUGH_CACHE.get(animal);
		if(pointer != null && !pointer.valid(animal)) { //invalid cache
			pointer = null;
			NEARBY_TROUGH_CACHE.remove(animal);
		}

		//there's no cached trough nearby.
		//Randomize whether we actually look for a new trough, to hopefully not eat all the tick time.
		if(pointer == null && level.random.nextFloat() <= lookChance) {
			BlockPos position = animal.blockPosition();
			pointer = level.getPoiManager().findClosest(
					holder -> holder.is(FEEDING_TROUGH_POI_KEY), p -> p.distSqr(position) <= range * range,
					position, (int) range, PoiManager.Occupancy.ANY)
				.flatMap(p -> TroughPointer.find(level, p, animal, temptations))
				.orElse(null);
			NEARBY_TROUGH_CACHE.put(animal, pointer);
		}

		//did we find one?
		if(pointer != null) {
			//if the animal can see it, direct the animal to this trough's fakeplayer
			BlockPos location = pointer.pos();
			Vec3 eyesPos = animal.position().add(0, animal.getEyeHeight(), 0);
			Vec3 targetPos = new Vec3(location.getX(), location.getY(), location.getZ()).add(0.5, 0.0625, 0.5);
			BlockHitResult ray = level.clip(new ClipContext(eyesPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, animal));
			if(ray.getType() == HitResult.Type.BLOCK && ray.getBlockPos().equals(location))
				return pointer.player();
		}

		return realPlayer;
	}

	@LoadEvent
	public final void register(ZRegister event) {
		feeding_trough = new FeedingTroughBlock("feeding_trough", this,
				Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().strength(0.6F).sound(SoundType.WOOD));

		blockEntityType = BlockEntityType.Builder.of(FeedingTroughBlockEntity::new, feeding_trough).build(null);
		event.getRegistry().register(blockEntityType, "feeding_trough", Registries.BLOCK_ENTITY_TYPE);

		PoiType feedingTroughPoi = new PoiType(ImmutableSet.copyOf(feeding_trough.getStateDefinition().getPossibleStates()), 1, 32);
		event.getRegistry().register(feedingTroughPoi, FEEDING_TROUGH_POI_KEY.location(), Registries.POINT_OF_INTEREST_TYPE);
	}

	private record TroughPointer(@NotNull BlockPos pos, @NotNull FakePlayer player) {

		static Optional<TroughPointer> find(Level level, BlockPos pos, Animal mob, Ingredient temptations) {
			if(level.getBlockEntity(pos) instanceof FeedingTroughBlockEntity trough) {
				FakePlayer fakeplayer = trough.getFoodHolder(mob, temptations);
				if(fakeplayer != null)
					return Optional.of(new TroughPointer(pos, fakeplayer));
			}
			return Optional.empty();
		}

		boolean valid(Animal animal) {
			return !player.isRemoved() && player.level() == animal.level() && pos.distSqr(animal.blockPosition()) <= range * range;
		}

	}
}
