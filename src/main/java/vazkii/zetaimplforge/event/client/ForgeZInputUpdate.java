package vazkii.zetaimplforge.event.client;

import net.minecraft.client.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import vazkii.zeta.client.event.ZInputUpdate;

public record ForgeZInputUpdate(MovementInputUpdateEvent e) implements ZInputUpdate {
	@Override
	public Input getInput() {
		return e.getInput();
	}

	@Override
	public Player getEntity() {
		return e.getEntity();
	}
}
