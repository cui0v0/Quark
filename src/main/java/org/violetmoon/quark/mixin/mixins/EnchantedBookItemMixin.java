package org.violetmoon.quark.mixin.mixins;

import net.minecraft.world.item.EnchantedBookItem;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantedBookItem.class)
public class EnchantedBookItemMixin {

	//TODO 1.20
//	@Inject(method = "fillItemCategory", at = @At("RETURN"))
//	private void canApply(CreativeModeTab tab, NonNullList<ItemStack> stacks, CallbackInfo ci) {
//		EnchantmentsBegoneModule.begoneItems(stacks);
//	}

}
