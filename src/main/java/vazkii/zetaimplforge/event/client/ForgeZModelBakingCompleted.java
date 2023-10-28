package vazkii.zetaimplforge.event.client;

import java.util.Map;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import vazkii.zeta.client.event.ZModelBakingCompleted;

public record ForgeZModelBakingCompleted(ModelEvent.BakingCompleted e) implements ZModelBakingCompleted {
	@Override
	public Map<ResourceLocation, BakedModel> getModels() {
		return e.getModels();
	}
}
