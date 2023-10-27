package vazkii.zeta.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;

public class ZetaPotionUtil {
	public static ItemStack of(Item potionType, Potion potion) {
		ItemStack stack = new ItemStack(potionType);
		PotionUtils.setPotion(stack, potion);
		return stack;
	}
}
