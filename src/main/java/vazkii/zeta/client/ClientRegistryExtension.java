package vazkii.zeta.client;

import java.util.function.Function;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vazkii.zeta.Zeta;
import vazkii.zeta.client.event.ZAddBlockColorHandlers;
import vazkii.zeta.client.event.ZAddItemColorHandlers;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.registry.ZetaRegistry;

public class ClientRegistryExtension {
	protected final Zeta z;
	protected final ZetaRegistry registry;

	public ClientRegistryExtension(Zeta z) {
		this.z = z;
		this.registry = z.registry;

		z.loadBus.subscribe(this);
	}

	@LoadEvent
	public void registerBlockColors(ZAddBlockColorHandlers.Post event) {
		registry.submitBlockColors((block, name) -> {
			Function<Block, BlockColor> blockColorCreator = event.getNamedBlockColors().get(name);
			if(blockColorCreator == null)
				z.log.error("Unknown block color creator {} used on block {}", name, block);
			else
				event.register(blockColorCreator.apply(block), block);
		});
	}

	@LoadEvent
	public void registerItemColors(ZAddItemColorHandlers.Post event) {
		registry.submitItemColors((item, name) -> {
			Function<Item, ItemColor> itemColorCreator = event.getNamedItemColors().get(name);
			if(itemColorCreator == null)
				z.log.error("Unknown item color creator {} used on item {}", name, item);
			else
				event.register(itemColorCreator.apply(item), item);
		});
	}
}
