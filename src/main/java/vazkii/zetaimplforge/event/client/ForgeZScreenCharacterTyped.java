package vazkii.zetaimplforge.event.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import vazkii.zeta.client.event.ZScreenCharacterTyped;
import vazkii.zeta.event.bus.FiredAs;

@FiredAs(ZScreenCharacterTyped.class)
public abstract class ForgeZScreenCharacterTyped implements ZScreenCharacterTyped {
	private final ScreenEvent.CharacterTyped e;

	public ForgeZScreenCharacterTyped(ScreenEvent.CharacterTyped e) {
		this.e = e;
	}

	@Override
	public Screen getScreen() {
		return e.getScreen();
	}

	@Override
	public char getCodePoint() {
		return e.getCodePoint();
	}

	@Override
	public int getModifiers() {
		return e.getModifiers();
	}

	@Override
	public boolean isCanceled() {
		return e.isCanceled();
	}

	@Override
	public void setCanceled(boolean cancel) {
		e.setCanceled(true);
	}

	@FiredAs(ZScreenCharacterTyped.Pre.class)
	public static class Pre extends ForgeZScreenCharacterTyped implements ZScreenCharacterTyped.Pre {
		public Pre(ScreenEvent.CharacterTyped.Pre e) {
			super(e);
		}
	}

	@FiredAs(ZScreenCharacterTyped.Post.class)
	public static class Post extends ForgeZScreenCharacterTyped implements ZScreenCharacterTyped.Post {
		public Post(ScreenEvent.CharacterTyped.Post e) {
			super(e);
		}
	}
}
