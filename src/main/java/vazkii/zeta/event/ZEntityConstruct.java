package vazkii.zeta.event;

import net.minecraft.world.entity.Entity;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZEntityConstruct extends IZetaPlayEvent {
	Entity getEntity();
}
