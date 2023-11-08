package org.violetmoon.zeta.event.bus.helpers;

import net.minecraft.world.entity.LivingEntity;

//fixme Move this into a lifelong home & make other interfaces use this more
public interface LivingGetter {
    LivingEntity getEntity();
}
