package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderTick extends IZetaPlayEvent {
	float getRenderTickTime();
	boolean isEndPhase();

	default boolean isStartPhase() {
		return !isEndPhase();
	}
}
