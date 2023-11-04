package vazkii.zeta.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZEntityInteract extends IZetaPlayEvent {
	Entity getTarget();
	Player getEntity();
	Level getLevel();
	InteractionHand getHand();
	ItemStack getItemStack();
}
