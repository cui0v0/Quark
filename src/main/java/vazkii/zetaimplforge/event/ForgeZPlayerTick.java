package vazkii.zetaimplforge.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import vazkii.zeta.event.ZPlayerTick;

public abstract class ForgeZPlayerTick implements ZPlayerTick {
	private final TickEvent.PlayerTickEvent e;

	protected ForgeZPlayerTick(TickEvent.PlayerTickEvent e) {
		this.e = e;
	}

	@Override
	public Player getPlayer() {
		return e.player;
	}

	public static class Start extends ForgeZPlayerTick implements ZPlayerTick.Start {
		public Start(TickEvent.PlayerTickEvent e) {
			super(e);
		}
	}

	public static class End extends ForgeZPlayerTick implements ZPlayerTick.End {
		public End(TickEvent.PlayerTickEvent e) {
			super(e);
		}
	}
}
