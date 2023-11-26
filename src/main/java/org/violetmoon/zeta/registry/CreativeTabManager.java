package org.violetmoon.zeta.registry;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreativeTabManager {
    private static final HashMap<ResourceKey<CreativeModeTab>, List<Item>> map = new HashMap<>();

    public static void addToCreativeTab(ResourceKey<CreativeModeTab> tab, Item item) {
        map.computeIfAbsent(tab, tabResourceKey -> new ArrayList<>()).add(item);
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        if (map.containsKey(tabKey)) {
            List<Item> items = map.get(tabKey);
            if (items != null) {
                for (Item item : items) {
                    event.accept(item);
                }
            }
        }
    }
}
