package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZClick extends IZetaPlayEvent {
	int getButton();
	int getAction();
}
