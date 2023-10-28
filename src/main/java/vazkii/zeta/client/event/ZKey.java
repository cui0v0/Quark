package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZKey extends IZetaPlayEvent {
	int getKey();
	int getScanCode();
	int getAction();
}
