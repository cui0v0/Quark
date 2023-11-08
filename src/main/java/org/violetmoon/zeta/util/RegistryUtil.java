package org.violetmoon.zeta.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

public class RegistryUtil {
	
	public static <T> List<T> massRegistryGet(Collection<String> coll, Registry<T> reg) {
		return coll.stream().map(ResourceLocation::new).map(reg::get).filter(Objects::nonNull).toList();
	}
	
}
