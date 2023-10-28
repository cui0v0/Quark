package vazkii.quark.base;

import vazkii.zeta.client.ClientTicker;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.util.ZetaSide;
import vazkii.zetaimplforge.client.ForgeZetaClient;

public class QuarkClient {

	static {
		if(Quark.ZETA.side == ZetaSide.SERVER)
			throw new IllegalAccessError("SOMEONE LOADED QuarkClient ON THE SERVER!!!! DON'T DO THAT!!!!!!");
	}

	public static QuarkClient instance;

	public static final ZetaClient ZETA_CLIENT = new ForgeZetaClient(Quark.ZETA);
	public static final ClientTicker ticker = ZETA_CLIENT.ticker;

	public static final String MISC_GROUP = "quark.gui.keygroup.misc";
	public static final String INV_GROUP = "quark.gui.keygroup.inv";
	public static final String EMOTE_GROUP = "quark.gui.keygroup.emote";

	public static void start() {
		instance = new QuarkClient();

		ZETA_CLIENT.start();
	}
}
