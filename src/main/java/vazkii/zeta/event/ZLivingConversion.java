package vazkii.zeta.event;

import net.minecraft.world.entity.LivingEntity;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.helpers.LivingGetter;

public interface ZLivingConversion extends IZetaPlayEvent, LivingGetter {
    interface Post extends ZLivingConversion {
        LivingEntity getOutcome();
    }
}
