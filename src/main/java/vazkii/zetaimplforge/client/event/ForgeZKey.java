package vazkii.zetaimplforge.client.event;

import net.minecraftforge.client.event.InputEvent;
import vazkii.zeta.client.event.ZKey;

public record ForgeZKey(InputEvent.Key e) implements ZKey {
	@Override
	public int getKey() {
		return e.getKey();
	}

	@Override
	public int getScanCode() {
		return e.getScanCode();
	}

	@Override
	public int getAction() {
		return e.getAction();
	}
}
