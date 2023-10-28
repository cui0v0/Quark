package vazkii.zetaimplforge.event.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import vazkii.zeta.client.event.ZScreenMousePressed;
import vazkii.zeta.event.bus.FiredAs;

@FiredAs(ZScreenMousePressed.class)
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

	@FiredAs(ZScreenMousePressed.Pre.class)
	public static class Pre extends ForgeZScreenMousePressed implements ZScreenMousePressed.Pre {
		public Pre(ScreenEvent.MouseButtonPressed.Pre e) {
			super(e);
		}
	}

	@FiredAs(ZScreenMousePressed.Post.class)
	public static class Post extends ForgeZScreenMousePressed implements ZScreenMousePressed.Post {
		public Post(ScreenEvent.MouseButtonPressed.Post e) {
			super(e);
		}
	}
}
