package org.violetmoon.quark.content.tools.item;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.api.IRuneColorProvider;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;

/**
 * @author WireSegal
 * Created at 2:27 PM on 8/17/19.
 */
public class RuneItem extends ZetaItem implements IRuneColorProvider {

	private final int color;
	private final boolean glow;

	public RuneItem(String regname, ZetaModule module, int color, boolean glow) {
		super(regname, module, new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
		this.color = color;
		this.glow = glow;
	}

	@Override
	public boolean isFoil(@NotNull ItemStack stack) {
		return glow;
	}

	@Override
	public int getRuneColor(ItemStack stack) {
		return color;
	}
}
