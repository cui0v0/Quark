package vazkii.zetaimplforge.event;

import net.minecraftforge.event.entity.player.PlayerEvent;
import vazkii.zeta.event.ZPlayerLoggedIn;

public class ForgeZPlayerLoggedIn implements ZPlayerLoggedIn {
	private final PlayerEvent.PlayerLoggedInEvent e;

	public ForgeZPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
		this.e = e;
	}
}
