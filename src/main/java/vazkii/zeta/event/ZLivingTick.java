package vazkii.zeta.event;

import net.minecraft.world.entity.LivingEntity;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZLivingTick extends IZetaPlayEvent {
	LivingEntity getEntity();
}
