package vazkii.zeta.client;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.Zeta;
import vazkii.zeta.client.config.ClientConfigManager;
import vazkii.zeta.event.bus.IZetaLoadEvent;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.ZetaEventBus;
import vazkii.zeta.registry.DyeablesRegistry;

public abstract class ZetaClient {
	public ZetaClient(Zeta zeta) {
		this.zeta = zeta;
		this.loadBus = zeta.loadBus;
		this.playBus = zeta.playBus;

		this.ticker = createClientTicker();
		this.clientConfigManager = createClientConfigManager();
		this.topLayerTooltipHandler = createTopLayerTooltipHandler();
		this.clientDyeablesRegistry = createClientDyeablesRegistry();
		this.clientRegistryExtension = createClientRegistryExtension();

		playBus.subscribe(topLayerTooltipHandler);
	}

	public final Zeta zeta;
	protected final ZetaEventBus<IZetaLoadEvent> loadBus;
	protected final ZetaEventBus<IZetaPlayEvent> playBus;

	public ResourceLocation generalIcons = new ResourceLocation("zeta", "textures/gui/general_icons.png");

	public final ClientTicker ticker;
	public final ClientConfigManager clientConfigManager;
	public final TopLayerTooltipHandler topLayerTooltipHandler;
	public final DyeablesRegistry.Client clientDyeablesRegistry;
	public final ClientRegistryExtension clientRegistryExtension;

	public ClientTicker createClientTicker() {
		return new ClientTicker(this);
	}

	public ClientConfigManager createClientConfigManager() {
		return new ClientConfigManager();
	}

	public TopLayerTooltipHandler createTopLayerTooltipHandler() {
		return new TopLayerTooltipHandler();
	}

	public DyeablesRegistry.Client createClientDyeablesRegistry() {
		return zeta.dyeables.new Client(this);
	}

	public ClientRegistryExtension createClientRegistryExtension() {
		return new ClientRegistryExtension(this.zeta);
	}

	//forge makes these weird
	public abstract @Nullable BlockColor getBlockColor(BlockColors bcs, Block block);
	public abstract @Nullable ItemColor getItemColor(ItemColors ics, ItemLike itemlike);

	public abstract void start();
}
