package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.FiredAs;
import vazkii.zeta.event.bus.IZetaPlayEvent;

@FiredAs(ZEndClientTick.class)
public class ZEndClientTick implements IZetaPlayEvent { }
