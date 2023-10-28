package vazkii.zetaimplforge.event.client;

import net.minecraftforge.client.event.InputEvent;
import vazkii.zeta.client.event.ZKey;
import vazkii.zeta.event.bus.FiredAs;

@FiredAs(ZKey.class)
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
