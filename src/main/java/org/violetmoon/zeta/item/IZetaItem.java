package org.violetmoon.zeta.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.violetmoon.zeta.module.IDisableable;
import org.violetmoon.zeta.registry.CreativeTabManager;

public interface IZetaItem extends IDisableable<IZetaItem> {

	default Item getItem() {
		return (Item) this;
	}

	default Item setCreativeTab(ResourceKey<CreativeModeTab> tab) {
		Item i = getItem();
		CreativeTabManager.addToCreativeTab(tab, i);
		return i;
	}

}
