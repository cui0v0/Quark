package vazkii.zeta.client;

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
	}

	public final Zeta zeta;
	protected final ZetaEventBus<IZetaLoadEvent> loadBus;
	protected final ZetaEventBus<IZetaPlayEvent> playBus;

	public final ClientTicker ticker;
	public final ClientConfigManager clientConfigManager;

	public abstract void start();
}
