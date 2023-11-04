package vazkii.zetaimplforge.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import vazkii.zeta.event.ZPlayerLoggedIn;

public class ForgeZPlayerLoggedIn implements ZPlayerLoggedIn {
	private final PlayerEvent.PlayerLoggedInEvent e;

	public ForgeZPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
		this.e = e;
	}

	@Override
	public Player getEntity() {
		return e.getEntity();
	}
}
