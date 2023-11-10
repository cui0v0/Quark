package org.violetmoon.zetaimplforge.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeMod;
import org.violetmoon.quark.base.handler.RaytracingUtil;

public class ForgeRaytracingUtil extends RaytracingUtil {
	@Override
	public double getEntityRange(LivingEntity player) {
		return player.getAttribute(ForgeMod.REACH_DISTANCE.get()).getValue();
	}
}
