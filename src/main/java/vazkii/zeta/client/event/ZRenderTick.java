package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.FiredAs;
import vazkii.zeta.event.bus.IZetaPlayEvent;

@FiredAs(ZRenderTick.class)
public interface ZRenderTick extends IZetaPlayEvent {
	boolean isEndPhase();
}
