package org.violetmoon.quark.mixin.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.resources.sounds.MinecartSoundInstance;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.content.client.module.WoolShutsUpMinecartsModule;

@Mixin(MinecartSoundInstance.class)
public class MinecartSoundInstanceMixin {

	@Shadow
	@Final
	private AbstractMinecart minecart;

	@ModifyExpressionValue(method = "tick()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/resources/sounds/MinecartSoundInstance;volume:F", opcode = Opcodes.PUTFIELD))
	public float muteIfOnWool(float volumeToSet) {
		if (volumeToSet > 0 && !WoolShutsUpMinecartsModule.canPlay(minecart))
			return 0;
		return volumeToSet;
	}

}
