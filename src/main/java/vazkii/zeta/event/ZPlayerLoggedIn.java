package vazkii.zeta.event;

import net.minecraft.world.entity.player.Player;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZPlayerLoggedIn extends IZetaPlayEvent {
	Player getEntity();
}
