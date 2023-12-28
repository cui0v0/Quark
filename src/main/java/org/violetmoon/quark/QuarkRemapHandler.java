package org.violetmoon.quark;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.MissingMappingsEvent;
import org.violetmoon.quark.base.Quark;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class QuarkRemapHandler {
    //datafixers could have also been used here but good luck figuring them out

    private static final Map<String, String> REMAP = new HashMap<>();

    static {
        REMAP.put("quark:crafter", "minecraft:crafter");
        REMAP.put("quark:polished_tuff", "minecraft:polished_tuff");
    }

    @SubscribeEvent
    public static void onRemapBlocks(MissingMappingsEvent event) {
        remapAll(event, BuiltInRegistries.BLOCK);
        remapAll(event, BuiltInRegistries.ITEM);
    }


    private static <T> void remapAll(MissingMappingsEvent event, DefaultedRegistry<T> block) {
        for (var v : event.getMappings(block.key(), Quark.MOD_ID)) {
            String rem = REMAP.get(v.getKey().toString());
            if (rem != null) {
                var b = block.getOptional(new ResourceLocation(rem));
                b.ifPresent(v::remap);
            } else v.ignore();
        }
    }
}
