package org.violetmoon.quark.mixin.mixins;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.violetmoon.quark.content.automation.module.FeedingTroughModule;

@Mixin(TemptGoal.class)
public class TemptGoalMixin {

	@Shadow
	protected Player player;

	@Shadow
	@Final
	public PathfinderMob mob;

	@Unique
	private long nextScheduledStart;

	private static final int RATE = 20;

	@Inject(method = "canUse", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/ai/goal/TemptGoal;player:Lnet/minecraft/world/entity/player/Player;", ordinal = 0, shift = At.Shift.AFTER))
	private void findTroughs(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		if(mob.level() instanceof ServerLevel level && mob instanceof Animal animal) {
			if(nextScheduledStart == 0L) {
				nextScheduledStart = level.getGameTime() + level.random.nextInt(RATE);
			} else if(level.getGameTime() >= nextScheduledStart)
				player = FeedingTroughModule.modifyTemptGoal(player, (TemptGoal) (Object) this, animal, level);
		}
	}

	@Inject(method = "start", at = @At(value = "HEAD"))
	private void updateSchedule(CallbackInfo ci) {
		nextScheduledStart = mob.level().getGameTime() + mob.level().random.nextInt(RATE);
	}
}
