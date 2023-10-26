package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.FiredAs;
import vazkii.zeta.event.bus.IZetaPlayEvent;

@FiredAs(ZEndRenderTick.class)
public class ZEndRenderTick implements IZetaPlayEvent { }
