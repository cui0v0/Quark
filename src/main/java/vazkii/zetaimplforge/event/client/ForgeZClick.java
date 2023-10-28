package vazkii.zetaimplforge.event.client;

import net.minecraftforge.client.event.InputEvent;
import vazkii.zeta.client.event.ZClick;
import vazkii.zeta.event.bus.FiredAs;

@FiredAs(ZClick.class)
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
