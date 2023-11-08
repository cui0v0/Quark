package org.violetmoon.zeta.client.event.load;

import java.util.Map;

import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

public interface ZModelBakingCompleted extends IZetaLoadEvent {
	Map<ResourceLocation, BakedModel> getModels();
}
