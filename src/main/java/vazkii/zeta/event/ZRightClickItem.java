package vazkii.zeta.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.zeta.event.bus.Cancellable;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZRightClickItem extends IZetaPlayEvent, Cancellable {
	Player getEntity();
	ItemStack getItemStack();
	InteractionHand getHand();
	Level getLevel();

	void setCancellationResult(InteractionResult result);
}
