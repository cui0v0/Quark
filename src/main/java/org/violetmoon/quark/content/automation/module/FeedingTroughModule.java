package org.violetmoon.quark.content.automation.module;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
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

	public static BlockEntityType<FeedingTroughBlockEntity> blockEntityType;
	public static PoiType feedingTroughPoi;
	@Hint
	Block feeding_trough;

	private static final String TAG_CACHE = "quark:feedingTroughCache";

	public static final Predicate<Holder<PoiType>> IS_FEEDER = (holder) -> holder.is(BuiltInRegistries.POINT_OF_INTEREST_TYPE.getKey(feedingTroughPoi));

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

	@Config(description = "Set to false to make it so animals look for a nearby trough every time they want to eat instead of remembering the last one. Can affect performance if false.")
	public static boolean enableTroughCaching = true;

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

		//locating the feeding trough
		TroughPointer pointer = null;

		//is there a cached one?
		boolean cached = false;
		if(enableTroughCaching) {
			TroughPointer candidate = TroughPointer.loadCached(animal, temptations);
			if(candidate != null) {
				pointer = candidate;
				cached = true;
			}
		}

		//if not, try locating a trough in the world
		if(!cached) {
			Vec3 position = animal.position();
			pointer = level.getPoiManager().findAllClosestFirstWithType(
					IS_FEEDER, p -> p.distSqr(new Vec3i((int) position.x, (int) position.y, (int) position.z)) <= range * range,
					animal.blockPosition(), (int) range, PoiManager.Occupancy.ANY)
				.map(Pair::getSecond)
				.map(pos -> getTroughPointer(level, pos, animal, temptations))
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
		}

		//did we find one?
		if(pointer != null && pointer.exists()) {
			//can the animal see it?
			BlockPos location = pointer.pos();
			Vec3 eyesPos = animal.position().add(0, animal.getEyeHeight(), 0);
			Vec3 targetPos = new Vec3(location.getX(), location.getY(), location.getZ()).add(0.5, 0.0625, 0.5);
			BlockHitResult ray = level.clip(new ClipContext(eyesPos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, animal));

			if(ray.getType() == HitResult.Type.BLOCK && ray.getBlockPos().equals(location)) {
				if(!cached)
					pointer.saveCache(animal);

				//direct the mob to this feeding trough's fakeplayer
				return pointer.player();
			}
		}

		// if we got here that means the cache is invalid
		if(cached)
			animal.getPersistentData().remove(TAG_CACHE);

		return realPlayer;
	}

	private static @Nullable TroughPointer getTroughPointer(Level level, BlockPos pos, Animal mob, Ingredient temptations) {
		if(level.getBlockEntity(pos) instanceof FeedingTroughBlockEntity trough)
			return new TroughPointer(pos, trough.getFoodHolder(mob, temptations));

		return null;
	}

	@LoadEvent
	public final void register(ZRegister event) {
		feeding_trough = new FeedingTroughBlock("feeding_trough", this,
				Block.Properties.of().mapColor(MapColor.WOOD).ignitedByLava().strength(0.6F).sound(SoundType.WOOD));

		blockEntityType = BlockEntityType.Builder.of(FeedingTroughBlockEntity::new, feeding_trough).build(null);
		Quark.ZETA.registry.register(blockEntityType, "feeding_trough", Registries.BLOCK_ENTITY_TYPE);

		feedingTroughPoi = new PoiType(getBlockStates(feeding_trough), 1, 32);
		Quark.ZETA.registry.register(feedingTroughPoi, "feeding_trough", Registries.POINT_OF_INTEREST_TYPE);
	}

	private static Set<BlockState> getBlockStates(Block p_218074_) {
		return ImmutableSet.copyOf(p_218074_.getStateDefinition().getPossibleStates());
	}

	private record TroughPointer(BlockPos pos, FakePlayer player) {

		public boolean exists() {
			return player != null;
		}

		public void saveCache(Entity e) {
			CompoundTag data = e.getPersistentData();
			CompoundTag tag = new CompoundTag();
			tag.putInt("x", pos.getX());
			tag.putInt("y", pos.getY());
			tag.putInt("z", pos.getZ());

			data.put(TAG_CACHE, tag);
		}

		public static TroughPointer loadCached(Animal mob, Ingredient temptations) {
			CompoundTag data = mob.getPersistentData();
			if(!data.contains(TAG_CACHE, data.getId()))
				return null;

			CompoundTag tag = data.getCompound(TAG_CACHE);
			int x = tag.getInt("x");
			int y = tag.getInt("y");
			int z = tag.getInt("z");

			BlockPos pos = new BlockPos(x, y, z);
			return getTroughPointer(mob.level(), pos, mob, temptations);
		}

	}
}
