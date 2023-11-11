package org.violetmoon.zeta.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class RegistryUtil {
	
	public static <T> List<T> massRegistryGet(Collection<String> coll, Registry<T> reg) {
		return coll.stream().map(ResourceLocation::new).map(reg::get).filter(Objects::nonNull).toList();
	}

	public static <T> List<T> getTagValues(RegistryAccess access, TagKey<T> tag) {
		Registry<T> registry = access.registryOrThrow(tag.registry());
		HolderSet<T> holderSet = registry.getTag(tag).orElse(new HolderSet.Named<>(registry, tag));

		return holderSet.stream().map(Holder::value).toList();
	}
}
