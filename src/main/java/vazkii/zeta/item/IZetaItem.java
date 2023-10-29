package vazkii.zeta.item;

import net.minecraft.world.item.Item;
import vazkii.zeta.module.IDisableable;

public interface IZetaItem extends IDisableable<IZetaItem> {

	default Item getItem() {
		return (Item) this;
	}

}
