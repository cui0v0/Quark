package vazkii.zeta.event;

import net.minecraft.core.BlockPos;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.helpers.LivingGetter;
import vazkii.zeta.event.bus.Resultable;

public interface ZSleepingLocationCheck extends IZetaPlayEvent, LivingGetter, Resultable {
    BlockPos getSleepingLocation();
}
