package vazkii.zetaimplforge.client;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.Block;
import vazkii.zeta.Zeta;
import vazkii.zeta.client.ClientRegistryExtension;
import vazkii.zeta.registry.RenderLayerRegistry;

public class ForgeClientRegistryExtension extends ClientRegistryExtension {
	public ForgeClientRegistryExtension(Zeta z) {
		super(z);
	}

	//Forge has some weirdo extension, they want you to use json or something.
	//Doing it from java is easier and more akin to how it happens on Fabric.
	@Override
	@SuppressWarnings("removal")
	protected void doSetRenderLayer(Block block, RenderLayerRegistry.Layer layer) {
		ItemBlockRenderTypes.setRenderLayer(block, resolvedTypes.get(layer));
	}
}
