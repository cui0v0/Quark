package org.violetmoon.zeta.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.client.config.ClientConfigManager;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.event.bus.ZetaEventBus;

public abstract class ZetaClient {
	public ZetaClient(Zeta zeta) {
		this.zeta = zeta;
		this.loadBus = zeta.loadBus;
		this.playBus = zeta.playBus;

		this.ticker = createClientTicker();
		this.clientConfigManager = createClientConfigManager();
		this.topLayerTooltipHandler = createTopLayerTooltipHandler();
		this.clientRegistryExtension = createClientRegistryExtension();

		loadBus.subscribe(clientRegistryExtension);

		playBus.subscribe(ticker);
		playBus.subscribe(topLayerTooltipHandler);
	}

	public final Zeta zeta;
	protected final ZetaEventBus<IZetaLoadEvent> loadBus;
	protected final ZetaEventBus<IZetaPlayEvent> playBus;

	public ResourceLocation generalIcons = new ResourceLocation("zeta", "textures/gui/general_icons.png");

	public final ClientTicker ticker;
	public final ClientConfigManager clientConfigManager;
	public final TopLayerTooltipHandler topLayerTooltipHandler;
	public final ClientRegistryExtension clientRegistryExtension;

	public ClientTicker createClientTicker() {
		return new ClientTicker();
	}

	public ClientConfigManager createClientConfigManager() {
		return new ClientConfigManager();
	}

	public TopLayerTooltipHandler createTopLayerTooltipHandler() {
		return new TopLayerTooltipHandler();
	}

	//kinda a grab bag of stuff that needs to happen client-only; hmm, not the best design
	public abstract ClientRegistryExtension createClientRegistryExtension();

	//forge makes these weird
	public abstract @Nullable BlockColor getBlockColor(BlockColors bcs, Block block);
	public abstract @Nullable ItemColor getItemColor(ItemColors ics, ItemLike itemlike);

	public abstract void setBlockEntityWithoutLevelRenderer(Item item, BlockEntityWithoutLevelRenderer bewlr);
	public abstract void setHumanoidArmorModel(Item item, HumanoidArmorModelGetter modelGetter);

	public abstract void start();
}
