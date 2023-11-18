package org.violetmoon.zeta.util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.block.IZetaBlock;

public class RegistryUtil {
	
	public static <T> List<T> massRegistryGet(Collection<String> coll, Registry<T> reg) {
		return coll.stream().map(ResourceLocation::new).map(reg::get).filter(Objects::nonNull).toList();
	}

	public static <T> List<T> getTagValues(RegistryAccess access, TagKey<T> tag) {
		Registry<T> registry = access.registryOrThrow(tag.registry());
		HolderSet<T> holderSet = registry.getTag(tag).orElse(new HolderSet.Named<>(registry, tag));

		return holderSet.stream().map(Holder::value).toList();
	}

	//TODO: Can be made more 'static' when there's a nicer way to get a block's ID, instead of having to consult a particular Zeta
	// (this is one reason i want to write the fancier block registry system - quat)
	// This is just Duct taped as fuck to get quark-specific stuff out of IQuarkBlock so i can make it IZetaBlock

	protected final Zeta z;

	public RegistryUtil(Zeta z) {
		this.z = z;
	}

	public String inheritQuark(IZetaBlock parent, String format) {
		return inherit(parent.getBlock(), format);
	}

	public String inherit(Block parent, String format) {
		ResourceLocation parentName = z.registry.getRegistryName(parent, BuiltInRegistries.BLOCK);
		return String.format(String.format("%s:%s", z.modid, format), parentName.getPath());
	}

	public String inherit(Block parent, Function<String, String> fun) {
		ResourceLocation parentName = z.registry.getRegistryName(parent, BuiltInRegistries.BLOCK);
		return String.format(String.format("%s:%s", z.modid, fun.apply(parentName.getPath())));
	}
}
