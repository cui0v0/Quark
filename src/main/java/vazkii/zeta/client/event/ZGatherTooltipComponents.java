package vazkii.zeta.client.event;

import java.util.List;

import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZGatherTooltipComponents extends IZetaPlayEvent {
	ItemStack getItemStack();
	int getScreenWidth();
	int getScreenHeight();
	List<Either<FormattedText, TooltipComponent>> getTooltipElements();
	int getMaxWidth();
	void setMaxWidth(int maxWidth);
}
