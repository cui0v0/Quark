package org.violetmoon.zeta.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.violetmoon.zeta.module.IDisableable;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

public class CreativeTabManager {

	private static final Object MUTEX = new Object();

	private static final Map<ResourceKey<CreativeModeTab>, CreativeTabAdditions> additions = new HashMap<>();
	private static final Multimap<ItemLike, ResourceKey<CreativeModeTab>> mappedItems = HashMultimap.create();

	public static void addToCreativeTab(ResourceKey<CreativeModeTab> tab, ItemLike item) {
		getForTab(tab).appendToEnd.add(item);
		mappedItems.put(item, tab);
	}

	public static void addToCreativeTabInFrontOf(ResourceKey<CreativeModeTab> tab, ItemLike item, ItemLike target) {
		tab = guessTab(target, tab);
		getForTab(tab).appendInFront.put(item, target);
		mappedItems.put(item, tab);
	}

	public static void addToCreativeTabBehind(ResourceKey<CreativeModeTab> tab, ItemLike item, ItemLike target) {
		tab = guessTab(target, tab);
		getForTab(tab).appendBehind.put(item, target);
		mappedItems.put(item, tab);
	}
	
	private static ResourceKey<CreativeModeTab> guessTab(ItemLike parent, ResourceKey<CreativeModeTab> tab) {
		if(parent != null && mappedItems.containsKey(parent))
			tab = mappedItems.get(parent).iterator().next();
		
		return tab;
	}
	
	private static CreativeTabAdditions getForTab(ResourceKey<CreativeModeTab> tab) {
		return additions.computeIfAbsent(tab, tabRk -> new CreativeTabAdditions());
	}

	public static void buildContents(BuildCreativeModeTabContentsEvent event) {
		synchronized(MUTEX) {
			ResourceKey<CreativeModeTab> tabKey = event.getTabKey();

			if(additions.containsKey(tabKey)) {
				CreativeTabAdditions add = additions.get(tabKey);

				for(ItemLike item : add.appendToEnd)
					acceptItem(event, item);

				MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
				
				Map<ItemLike, ItemLike> front = new LinkedHashMap<>(add.appendInFront);
				Map<ItemLike, ItemLike> behind = new LinkedHashMap<>(add.appendBehind);
				int misses = 0;
				
            	while(true) {
            		boolean missed = false; 
            		if(!front.isEmpty())
            			missed = appendNextTo(tabKey, entries, front, false);
            		if(!behind.isEmpty())
            			missed |= appendNextTo(tabKey, entries, behind, true);

            		if(missed)
            			misses++;
            		
            		// arbitrary failsafe, should never happen
            		if(misses > 1e6) {
            			new RuntimeException("Creative tab placement misses exceeded failsafe, aborting logic").printStackTrace();
            			return;
            		}
            		
            		if(front.isEmpty() && behind.isEmpty())
            			return;
            	}
			}
		}
	}

	private static boolean isItemEnabled(ItemLike item) {
		if(item instanceof IDisableable<?> id)
			return id.isEnabled();

		return true;
	}

	private static void acceptItem(BuildCreativeModeTabContentsEvent event, ItemLike item) {
		if(!isItemEnabled(item))
			return;
		
		if(item instanceof AppendsUniquely au)
			event.acceptAll(au.appendItemsToCreativeTab());
		else 
			event.accept(item);
	}

	private static void addToEntries(ItemStack target, MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries, ItemLike item, boolean behind) {
		List<ItemStack> stacksToAdd = Arrays.asList(new ItemStack(item));
		if(item instanceof AppendsUniquely au)
			stacksToAdd = au.appendItemsToCreativeTab();
		
		for(ItemStack addStack : stacksToAdd) {
			if(behind)
				entries.putBefore(target, addStack, TabVisibility.PARENT_AND_SEARCH_TABS);
			else 
				entries.putAfter(target, addStack, TabVisibility.PARENT_AND_SEARCH_TABS);
		}
	}

	/**
	 * Returns true if the item needs to be tried again later 
	 */
	private static boolean appendNextTo(ResourceKey<CreativeModeTab> tabKey, MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries, Map<ItemLike, ItemLike> map, boolean behind) {
		Collection<ItemLike> coll = map.keySet();
		if(coll.isEmpty())
			throw new RuntimeException("Tab collection is empty, this should never happen.");

		ItemLike firstItem = coll.iterator().next();
		ItemLike target = map.get(firstItem);
		
		map.remove(firstItem);
		
		if(!isItemEnabled(firstItem) || target == null)
			return false;
		
		for(Entry<ItemStack, TabVisibility> entry : entries) {
			ItemStack stack = entry.getKey();
			Item item = stack.getItem();
			if(item == target.asItem()) {
				addToEntries(stack, entries, firstItem, behind);
				
				return false;
			}
		}
		
		// put the item back at the end of the map to try it again after the target is added 
		map.put(firstItem, target);
		return true;
	}

	private static class CreativeTabAdditions {

		private List<ItemLike> appendToEnd = new ArrayList<>();
		private Map<ItemLike, ItemLike> appendInFront = new LinkedHashMap<>();
		private Map<ItemLike, ItemLike> appendBehind = new LinkedHashMap<>();
		
	}

	public interface AppendsUniquely extends ItemLike {
		List<ItemStack> appendItemsToCreativeTab();
	}
}
