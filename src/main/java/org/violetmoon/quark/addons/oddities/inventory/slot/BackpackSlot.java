package org.violetmoon.quark.addons.oddities.inventory.slot;

import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.module.BackpackModule;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class BackpackSlot extends CachedItemHandlerSlot {

	public BackpackSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}
	
	@Override
	public boolean mayPlace(@NotNull ItemStack stack) {
		return super.mayPlace(stack) && !stack.is(BackpackModule.backpackBlockedTag);
	}

}
