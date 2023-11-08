package org.violetmoon.zetaimplforge.client.event.load;

import java.util.Map;

import org.violetmoon.zeta.client.event.load.ZModelBakingCompleted;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;

public record ForgeZModelBakingCompleted(ModelEvent.BakingCompleted e) implements ZModelBakingCompleted {
	@Override
	public Map<ResourceLocation, BakedModel> getModels() {
		return e.getModels();
	}
}
