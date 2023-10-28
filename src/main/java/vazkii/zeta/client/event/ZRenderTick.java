package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderTick extends IZetaPlayEvent {
	boolean isEndPhase();
}
