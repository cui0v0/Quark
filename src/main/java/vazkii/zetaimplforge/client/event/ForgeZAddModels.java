package vazkii.zetaimplforge.client.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import vazkii.zeta.client.event.ZAddModels;

public record ForgeZAddModels(ModelEvent.RegisterAdditional e) implements ZAddModels {
	@Override
	public void register(ResourceLocation model) {
		e.register(model);
	}
}
