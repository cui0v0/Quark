package org.violetmoon.zeta.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.item.ZetaBlockItem;

//Mash of arl's RegistryHelper and its ModData innerclass.
//You're expected to create one of these per modid instead, avoiding a dependency on Forge's "current mod id" notion.
public abstract class ZetaRegistry {
	protected final Zeta z;

	// the keys of this are things like "minecraft:block", "minecraft:item" and so on
	private final Multimap<ResourceLocation, Supplier<Object>> defers = ArrayListMultimap.create();
	
	// to support calling getRegistryName before the object actually gets registered for real
	protected final Map<Object, ResourceLocation> internalNames = new IdentityHashMap<>();
	
	// "named color provider" system allows blocks and items to choose their own color providers in a side-safe way
	// TODO: should this go somewhere else and not be so tightly-integrated?
	private final Map<Block, String> blocksToColorProviderName = new HashMap<>();
	private final Map<Item, String> itemsToColorProviderName = new HashMap<>();

	// creative tab haxx TODO 1.20: replace, this is a dummy system just to *track* creative tabs
	private final Map<Block, String> creativeTabInfos = new LinkedHashMap<>();

	public ZetaRegistry(Zeta z) {
		this.z = z;
	}

	public <T> ResourceLocation getRegistryName(T obj, Registry<T> registry) {
		ResourceLocation internal = internalNames.get(obj);
		return internal == null ? registry.getKey(obj) : internal;
	}

	//You know how `new ResourceLocation(String)` prepends "minecraft" if there's no prefix?
	//This method is like that, except it prepends *your* modid
	public ResourceLocation newResourceLocation(String in) {
		if(in.indexOf(':') == -1) return new ResourceLocation(z.modid, in);
		else return new ResourceLocation(in);
	}

	//Root registration method
	public <T> void register(T obj, ResourceLocation id, ResourceKey<Registry<T>> registry) {
		if(obj == null)
			throw new IllegalArgumentException("Can't register null object.");

		if(obj instanceof Block block && obj instanceof IZetaBlockColorProvider provider && provider.getBlockColorProviderName() != null)
			blocksToColorProviderName.put(block, provider.getBlockColorProviderName());

		if(obj instanceof Item item && obj instanceof IZetaItemColorProvider provider && provider.getItemColorProviderName() != null)
			itemsToColorProviderName.put(item, provider.getItemColorProviderName());

		internalNames.put(obj, id);
		defers.put(registry.location(), () -> obj);
	}

	public <T> void register(T obj, String resloc, ResourceKey<Registry<T>> registry) {
		register(obj, newResourceLocation(resloc), registry);
	}

	public void registerItem(Item item, ResourceLocation id) {
		register(item, id, Registries.ITEM);
	}

	public void registerItem(Item item, String resloc) {
		register(item, newResourceLocation(resloc), Registries.ITEM);
	}

	public void registerBlock(Block block, ResourceLocation id, boolean hasBlockItem) {
		register(block, id, Registries.BLOCK);

		//TODO: this supplier is mostly a load-bearing way to defer calling groups.get(registryName),
		// until after CreativeTabHandler.finalizeTabs is called
		if(hasBlockItem)
			defers.put(Registries.ITEM.location(), () -> createItemBlock(block));
	}

	public void registerBlock(Block block, String resloc, boolean hasBlockItem) {
		registerBlock(block, newResourceLocation(resloc), hasBlockItem);
	}

	public void registerBlock(Block block, ResourceLocation id) {
		registerBlock(block, id, true);
	}

	public void registerBlock(Block block, String resloc) {
		registerBlock(block, resloc, true);
	}

	@Deprecated
	public void setCreativeTab(IZetaBlock block, String tabName) {
		setCreativeTab(block.getBlock(), tabName, block::isEnabled);
	}

	@Deprecated
	public void setCreativeTab(Block block, String tabName, BooleanSupplier enabled) {
		//TODO delete
		creativeTabInfos.put(block, tabName);
	}

	private Item createItemBlock(Block block) {
		Item.Properties props = new Item.Properties();
		ResourceLocation registryName = internalNames.get(block);

		String tabInfo = creativeTabInfos.remove(block); //"remove" because we don't need these in-memory anymore
		//Quark.LOG.error("id {} tab {}", registryName, tabInfo); //TODO creative tabs are different

		if(block instanceof IZetaItemPropertiesFiller filler)
			filler.fillItemProperties(props);

		BlockItem blockitem;
		if(block instanceof IZetaBlockItemProvider)
			blockitem = ((IZetaBlockItemProvider) block).provideItemBlock(block, props);
		else blockitem = new ZetaBlockItem(block, props);

		if(block instanceof IZetaItemColorProvider prov && prov.getItemColorProviderName() != null)
			itemsToColorProviderName.put(blockitem, prov.getItemColorProviderName());

		internalNames.put(blockitem, registryName);
		return blockitem;
	}

	/// performing registration ///

	public Collection<Supplier<Object>> getDefers(ResourceLocation registryId) {
		return defers.get(registryId);
	}

	public void clearDeferCache(ResourceLocation resourceLocation) {
		defers.removeAll(resourceLocation);
	}

	public void finalizeBlockColors(BiConsumer<Block, String> consumer) {
		blocksToColorProviderName.forEach(consumer);
		blocksToColorProviderName.clear();
	}

	public void finalizeItemColors(BiConsumer<Item, String> consumer) {
		itemsToColorProviderName.forEach(consumer);
		itemsToColorProviderName.clear();
	}
}
