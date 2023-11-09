package org.violetmoon.zeta.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.violetmoon.zeta.item.ext.IZetaItemExtensions;

public class ZetaArmorItem extends ArmorItem implements IZetaItemExtensions {
	public ZetaArmorItem(ArmorMaterial mat, EquipmentSlot slot, Properties props) {
		super(mat, slot, props);
	}
}
