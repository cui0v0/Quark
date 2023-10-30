package vazkii.zeta.event;

import net.minecraft.world.item.ItemStack;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZFurnaceFuelBurnTime extends IZetaPlayEvent {
	ItemStack getItemStack();
	void setBurnTime(int time);
}
