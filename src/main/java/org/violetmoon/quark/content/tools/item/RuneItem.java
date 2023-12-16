package org.violetmoon.quark.content.tools.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import org.jetbrains.annotations.NotNull;

import org.violetmoon.quark.api.IRuneColorProvider;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

/**
 * @author WireSegal
 *         Created at 2:27 PM on 8/17/19.
 */
public class RuneItem extends ZetaItem implements IRuneColorProvider {

	private final int color;
	private final boolean glow;

	public RuneItem(String regname, ZetaModule module, int color, boolean glow) {
		super(regname, module, new Item.Properties());
		this.color = color;
		this.glow = glow;

		CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.INGREDIENTS, this, Items.EXPERIENCE_BOTTLE, true);
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
