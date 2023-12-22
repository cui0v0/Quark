package org.violetmoon.quark.mixin.mixins;

import com.mojang.datafixers.util.Pair;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.alchemy.PotionUtils;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(value = PotionUtils.class)
public class PotionUtilsMixin {

	@ModifyVariable(method = "addPotionTooltip(Ljava/util/List;Ljava/util/List;F)V", at = @At(value = "INVOKE", target = "Ljava/util/List;isEmpty()Z", ordinal = 1, shift = At.Shift.BEFORE), ordinal = 2)
	private static List<Pair<Attribute, AttributeModifier>> overrideAttributeTooltips(List<Pair<Attribute, AttributeModifier>> attributes, List<MobEffectInstance> mobEffects) {
		/*if(ImprovedTooltipsModule.shouldHideAttributes()) {
			((PseudoAccessorItemStack) (Object) stack).quark$capturePotionAttributes(attributes);
			return Collections.emptyList();
		}*/
		return attributes;
	}
}
