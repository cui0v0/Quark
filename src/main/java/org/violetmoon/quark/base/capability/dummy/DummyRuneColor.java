package org.violetmoon.quark.base.capability.dummy;

import org.violetmoon.quark.api.IRuneColorProvider;

import net.minecraft.world.item.ItemStack;

public class DummyRuneColor implements IRuneColorProvider {

	@Override
	public int getRuneColor(ItemStack stack) {
		return -1;
	}
}
