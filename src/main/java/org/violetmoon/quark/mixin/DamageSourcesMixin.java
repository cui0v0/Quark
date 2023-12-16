package org.violetmoon.quark.mixin;

import net.minecraft.world.damagesource.DamageSources;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(DamageSources.class)
public class DamageSourcesMixin {
	/*@Inject(method = "playerAttack", at = @At("HEAD"), cancellable = true)
	private static void playerAttack(Player player, CallbackInfoReturnable<DamageSource> callbackInfoReturnable) {
		DamageSource damage = PickarangModule.createDamageSource(player);
	
		if(damage != null)
			callbackInfoReturnable.setReturnValue(damage);
	}*/
}
