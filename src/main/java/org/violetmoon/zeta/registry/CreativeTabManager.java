package org.violetmoon.zeta.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.module.IDisableable;

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
    private static final List<ItemLike> mappedItems = new ArrayList<>();

    public static void addToCreativeTab(ResourceKey<CreativeModeTab> tab, ItemLike item) {
    	getForTab(tab).appendToEnd.add(item);
    	mappedItems.add(item);
    }
    
    public static void addToCreativeTabInFrontOf(ResourceKey<CreativeModeTab> tab, ItemLike item, ItemLike target) {
    	getForTab(tab).appendInFront.put(item, target);
    	mappedItems.add(item);
    }
    
    public static void addToCreativeTabBehind(ResourceKey<CreativeModeTab> tab, ItemLike item, ItemLike target) {
    	getForTab(tab).appendBehind.put(item, target);
    	mappedItems.add(item);
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
            		if(isItemEnabled(item))
            			event.accept(item);
            	
            	MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
            	while(true) {
            		if(!add.appendInFront.isEmpty())
            			appendNextTo(tabKey, entries, add.appendInFront, false);
            		if(!add.appendBehind.isEmpty())
            			appendNextTo(tabKey, entries, add.appendBehind, true);
            		
            		if(add.appendBehind.isEmpty() && add.appendInFront.isEmpty())
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
    
    private static void appendNextTo( ResourceKey<CreativeModeTab> tabKey, MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries, Map<ItemLike, ItemLike> map, boolean behind) {
    	Collection<ItemLike> coll = map.keySet();
    	if(coll.isEmpty())
    		throw new RuntimeException("Tab collection is empty, this should never happen.");
    	
    	ItemLike firstItem = coll.iterator().next();
    	ItemLike target = map.get(firstItem);
    	
    	map.remove(firstItem);
    	
    	if(!isItemEnabled(firstItem) || target == null)
    		return;
    	
    	for(Entry<ItemStack, TabVisibility> entry : entries) {
    		ItemStack stack = entry.getKey();
    		Item item = stack.getItem();
    		if(item == target.asItem()) {
    			if(behind)
    				entries.putBefore(stack, new ItemStack(firstItem), TabVisibility.PARENT_AND_SEARCH_TABS);
    			else 
    				entries.putAfter(stack, new ItemStack(firstItem), TabVisibility.PARENT_AND_SEARCH_TABS);
    			
    			return;
    		}
    	}
    	
    	Zeta.GLOBAL_LOG.error("{} is being added to creative tab {} next to {}, but the parent isn't present", firstItem, tabKey.location(), target);
    }
    
    private static class CreativeTabAdditions {
    	
    	private List<ItemLike> appendToEnd = new ArrayList<>();
    	private Map<ItemLike, ItemLike> appendInFront = new LinkedHashMap<>();
    	private Map<ItemLike, ItemLike> appendBehind = new LinkedHashMap<>();
    	
    	
    }
}
