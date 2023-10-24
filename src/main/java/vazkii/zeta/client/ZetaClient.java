package vazkii.zeta.client;

import vazkii.zeta.Zeta;
import vazkii.zeta.config.client.ClientConfigManager;
import vazkii.zeta.event.bus.IZetaLoadEvent;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.ZetaEventBus;

public abstract class ZetaClient {
	public ZetaClient(Zeta z) {
		this.z = z;
		this.loadBus = z.loadBus;
		this.playBus = z.playBus;

		//this.ticker = new ClientTicker();
		this.ticker = z.ticker_SHOULD_NOT_BE_HERE; //TODO, move ClientTicker into actual client code
		this.clientConfigManager = new ClientConfigManager();
	}

	protected final Zeta z;
	protected final ZetaEventBus<IZetaLoadEvent> loadBus;
	protected final ZetaEventBus<IZetaPlayEvent> playBus;

	public final ClientTicker ticker;
	public final ClientConfigManager clientConfigManager;

	public abstract void start();
}
