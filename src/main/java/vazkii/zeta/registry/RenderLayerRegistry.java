package vazkii.zeta.registry;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import vazkii.zeta.client.event.ZClientSetup;
import vazkii.zeta.event.bus.LoadEvent;

/**
 * note this is NOT client-side code, for ease of registering stuff in constructors
 */
public class RenderLayerRegistry {

	protected final Map<Block, Layer> mapping = new HashMap<>();
	protected final Map<Block, Block> inheritances = new HashMap<>();

	public enum Layer {
		SOLID,
		CUTOUT,
		CUTOUT_MIPPED,
		TRANSLUCENT,
	}

	public void put(Block block, Layer layer) {
		mapping.put(block, layer);
	}

	public void mock(Block block, Block inheritFrom) {
		inheritances.put(block, inheritFrom);
	}

	/**
	 * but this *is* client-side code :blush:
	 */
	public static class Client {

		protected final RenderLayerRegistry reg;
		protected final Map<Layer, RenderType> resolvedTypes = new EnumMap<>(Layer.class);

		public Client(RenderLayerRegistry reg) {
			this.reg = reg;

			resolvedTypes.put(Layer.SOLID, RenderType.solid());
			resolvedTypes.put(Layer.CUTOUT, RenderType.cutout());
			resolvedTypes.put(Layer.CUTOUT_MIPPED, RenderType.cutoutMipped());
			resolvedTypes.put(Layer.TRANSLUCENT, RenderType.translucent());
		}

		@LoadEvent
		public void register(ZClientSetup event) {
			//Note: only handles one layer of inheritances
			for(Block b : reg.inheritances.keySet()) {
				Block inheritFrom = reg.inheritances.get(b);
				Layer layer = reg.mapping.get(inheritFrom);

				if(layer != null)
					reg.mapping.put(b, layer);
			}

			reg.mapping.forEach(this::doSetRenderLayer);

			reg.inheritances.clear();
			reg.mapping.clear();
		}

		//Forge has some weirdo extension, they want you to use json or something.
		//Doing it from java is easier and more akin to how it happens on Fabric.
		@SuppressWarnings("removal")
		protected void doSetRenderLayer(Block block, Layer layer) {
			ItemBlockRenderTypes.setRenderLayer(block, resolvedTypes.get(layer));
		}

	}
}
