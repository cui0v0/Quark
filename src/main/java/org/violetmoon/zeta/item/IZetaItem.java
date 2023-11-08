package org.violetmoon.zeta.item;

import org.violetmoon.zeta.module.IDisableable;

import net.minecraft.world.item.Item;

public interface IZetaItem extends IDisableable<IZetaItem> {

	default Item getItem() {
		return (Item) this;
	}

}
