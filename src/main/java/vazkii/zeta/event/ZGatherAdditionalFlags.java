package vazkii.zeta.event;

import vazkii.quark.base.module.config.ConfigFlagManager;
import vazkii.zeta.event.bus.FiredAs;
import vazkii.zeta.event.bus.IZetaPlayEvent;

@FiredAs(ZGatherAdditionalFlags.class)
public record ZGatherAdditionalFlags(ConfigFlagManager flagManager) implements IZetaPlayEvent { }
