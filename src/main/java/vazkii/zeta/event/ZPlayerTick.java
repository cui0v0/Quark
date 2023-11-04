package vazkii.zeta.event;

import net.minecraft.world.entity.player.Player;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZPlayerTick extends IZetaPlayEvent {
	Player getPlayer();

	//extracting forge's event phases
	interface Start extends ZPlayerTick { }
	interface End extends ZPlayerTick { }
}
