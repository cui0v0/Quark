package vazkii.zeta.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import vazkii.zeta.event.bus.Cancellable;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.helpers.LivingGetter;

public interface ZLivingChangeTarget extends IZetaPlayEvent, Cancellable, LivingGetter {
    LivingEntity getNewTarget();
    LivingChangeTargetEvent.ILivingTargetType getTargetType();
}
