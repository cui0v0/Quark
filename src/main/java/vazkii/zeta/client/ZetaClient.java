package vazkii.zeta.client;

import net.minecraft.resources.ResourceLocation;
import vazkii.zeta.Zeta;
import vazkii.zeta.client.config.ClientConfigManager;
import vazkii.zeta.event.bus.IZetaLoadEvent;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.ZetaEventBus;

public abstract class ZetaClient {
	public ZetaClient(Zeta zeta) {
		this.zeta = zeta;
		this.loadBus = zeta.loadBus;
		this.playBus = zeta.playBus;

		//this.ticker = new ClientTicker();
		this.ticker = zeta.ticker_SHOULD_NOT_BE_HERE; //TODO, move ClientTicker into actual client code
		this.clientConfigManager = new ClientConfigManager();
		this.topLayerTooltipHandler = new TopLayerTooltipHandler();

		playBus.subscribe(topLayerTooltipHandler);
	}

	public final Zeta zeta;
	protected final ZetaEventBus<IZetaLoadEvent> loadBus;
	protected final ZetaEventBus<IZetaPlayEvent> playBus;

	public ResourceLocation generalIcons = new ResourceLocation("zeta", "textures/gui/general_icons.png");

	public final ClientTicker ticker;
	public final ClientConfigManager clientConfigManager;
	public final TopLayerTooltipHandler topLayerTooltipHandler;

	public abstract void start();
}
