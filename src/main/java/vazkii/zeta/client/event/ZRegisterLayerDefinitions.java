package vazkii.zeta.client.event;

import java.util.function.Supplier;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZRegisterLayerDefinitions extends IZetaLoadEvent {
	void registerLayerDefinition(ModelLayerLocation layerLocation, Supplier<LayerDefinition> supplier);
}
