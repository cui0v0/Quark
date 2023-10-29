package vazkii.zetaimplforge.client.event;

import net.minecraftforge.client.event.InputEvent;
import vazkii.zeta.client.event.ZClick;

public record ForgeZClick(InputEvent.MouseButton e) implements ZClick {
	@Override
	public int getButton() {
		return e.getButton();
	}

	@Override
	public int getAction() {
		return e.getAction();
	}
}
