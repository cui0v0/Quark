package vazkii.zeta.event.client;

import vazkii.zeta.event.bus.FiredAs;
import vazkii.zeta.event.bus.IZetaPlayEvent;

@FiredAs(ZEndClientTickEvent.class)
public class ZEndClientTickEvent implements IZetaPlayEvent { }
