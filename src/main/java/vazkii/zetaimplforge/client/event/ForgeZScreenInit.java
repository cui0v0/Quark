package vazkii.zetaimplforge.client.event;

import java.util.List;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import vazkii.zeta.client.event.ZScreenInit;

public class ForgeZScreenInit implements ZScreenInit {

	private final ScreenEvent.Init e;

	public ForgeZScreenInit(ScreenEvent.Init e) {
		this.e = e;
	}

	@Override
	public Screen getScreen() {
		return e.getScreen();
	}

	@Override
	public List<GuiEventListener> getListenersList() {
		return e.getListenersList();
	}

	@Override
	public void addListener(GuiEventListener listener) {
		e.addListener(listener);
	}

	@Override
	public void removeListener(GuiEventListener listener) {
		e.removeListener(listener);
	}

	public static class Pre extends ForgeZScreenInit implements ZScreenInit.Pre {
		public Pre(ScreenEvent.Init.Pre e) {
			super(e);
		}
	}

	public static class Post extends ForgeZScreenInit implements ZScreenInit.Post {
		public Post(ScreenEvent.Init.Post e) {
			super(e);
		}
	}
}
