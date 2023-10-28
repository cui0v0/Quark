package vazkii.zetaimplforge.event.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import vazkii.zeta.client.event.ZScreenMousePressed;

public class ForgeZScreenMousePressed implements ZScreenMousePressed {
	protected final ScreenEvent.MouseButtonPressed e;

	public ForgeZScreenMousePressed(ScreenEvent.MouseButtonPressed e) {
		this.e = e;
	}

	@Override
	public Screen getScreen() {
		return e.getScreen();
	}

	@Override
	public int getButton() {
		return e.getButton();
	}

	@Override
	public double getMouseX() {
		return e.getMouseX();
	}

	@Override
	public double getMouseY() {
		return e.getMouseY();
	}

	@Override
	public boolean isCanceled() {
		return e.isCanceled();
	}

	@Override
	public void setCanceled(boolean cancel) {
		e.setCanceled(cancel);
	}

	public static class Pre extends ForgeZScreenMousePressed implements ZScreenMousePressed.Pre {
		public Pre(ScreenEvent.MouseButtonPressed.Pre e) {
			super(e);
		}
	}

	public static class Post extends ForgeZScreenMousePressed implements ZScreenMousePressed.Post {
		public Post(ScreenEvent.MouseButtonPressed.Post e) {
			super(e);
		}
	}
}
