package vazkii.zeta.client.config.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import vazkii.zeta.Zeta;
import vazkii.zeta.client.ZetaClient;

public abstract class ZetaScreen extends Screen {

	private final Screen parent;

	protected final Zeta z;
	protected final ZetaClient zc;

	public ZetaScreen(ZetaClient zc, Component title, Screen parent) {
		super(title);
		this.parent = parent;

		this.z = zc.zeta;
		this.zc = zc;
	}

	public ZetaScreen(ZetaClient zc, Screen parent) {
		this(zc, Component.empty(), parent);
	}

	public void returnToParent() {
		minecraft.setScreen(parent);
	}
}
