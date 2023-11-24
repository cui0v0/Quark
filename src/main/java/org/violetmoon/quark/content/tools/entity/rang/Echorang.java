package org.violetmoon.quark.content.tools.entity.rang;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tools.config.PickarangType;
import org.violetmoon.quark.content.tools.module.PickarangModule;

import static org.violetmoon.quark.content.tools.module.PickarangModule.echorangBreaksAnywayTag;

import java.util.function.BiConsumer;

public class Echorang extends AbstractPickarang<Echorang> implements VibrationSystem {
	private final EchorangVibrationUser user = new EchorangVibrationUser();
	private Data data = new Data();

	private final DynamicGameEventListener<VibrationSystem.Listener> vibrationListener;

	public Echorang(EntityType<Echorang> type, Level worldIn) {
		super(type, worldIn);
		vibrationListener = makeListener();
	}

	public Echorang(EntityType<Echorang> type, Level worldIn, Player thrower) {
		super(type, worldIn, thrower);
		vibrationListener = makeListener();
	}

	private DynamicGameEventListener<VibrationSystem.Listener> makeListener() {
		return new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
	}

	@Override
	protected void emitParticles(Vec3 pos, Vec3 ourMotion) {
		if(Math.random() < 0.4)
			this.level().addParticle(ParticleTypes.SCULK_SOUL,
					pos.x - ourMotion.x * 0.25D + (Math.random() - 0.5) * 0.4,
					pos.y - ourMotion.y * 0.25D + (Math.random() - 0.5) * 0.4,
					pos.z - ourMotion.z * 0.25D + (Math.random() - 0.5) * 0.4,
					(Math.random() - 0.5) * 0.1,
					(Math.random() - 0.5) * 0.1,
					(Math.random() - 0.5) * 0.1);
	}

	@Override
	protected boolean canDestroyBlock(BlockState state) {
		return super.canDestroyBlock(state) || state.is(echorangBreaksAnywayTag);
	}

	@Override
	public boolean hasDrag() {
		return false;
	}

	@Override
	public void tick() {
		super.tick();

		gameEvent(GameEvent.PROJECTILE_SHOOT);

		if (level() instanceof ServerLevel serverlevel)
			VibrationSystem.Ticker.tick(serverlevel, getVibrationData(), getVibrationUser());
	}

	@Override
	public PickarangType<Echorang> getPickarangType() {
		return PickarangModule.echorangType;
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);

		Data.CODEC.encodeStart(NbtOps.INSTANCE, data).resultOrPartial(Quark.LOG::error).ifPresent((nbt) -> compound.put("listener", nbt));
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compound) {
		super.readAdditionalSaveData(compound);

		if(compound.contains("listener", 10))
			Data.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))).resultOrPartial(Quark.LOG::error).ifPresent((nbt) -> data = nbt);
	}

	@Override
	public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> consumer) {
		if (level() instanceof ServerLevel serverlevel)
			consumer.accept(this.vibrationListener, serverlevel);
	}

	@Override
	public @NotNull Data getVibrationData() {
		return data;
	}

	@Override
	public @NotNull User getVibrationUser() {
		return user;
	}

	public class EchorangVibrationUser implements VibrationSystem.User {
		@Override
		public int getListenerRadius() {
			return 16;
		}

		@Override
		public @NotNull PositionSource getPositionSource() {
			return new EntityPositionSource(Echorang.this, Echorang.this.getEyeHeight());
		}

		@Override
		public boolean canReceiveVibration(ServerLevel level, BlockPos pos, GameEvent event, Context context) {
			return !isReturning() && level.getWorldBorder().isWithinBounds(pos) && !isRemoved() && Echorang.this.level() == level;
		}

		@Override
		public void onReceiveVibration(ServerLevel level, BlockPos pos, GameEvent event, @Nullable Entity receiving, @Nullable Entity projectileOwner, float distance) {
			liveTime = 0;
		}

		@Override
		public TagKey<GameEvent> getListenableEvents() {
			return PickarangModule.echorangCanListenTag;
		}

		@Override
		public boolean isValidVibration(GameEvent gameEvent, Context context) {
			return gameEvent.is(getListenableEvents()) && context.sourceEntity() == getOwner();
		}
	}
}
