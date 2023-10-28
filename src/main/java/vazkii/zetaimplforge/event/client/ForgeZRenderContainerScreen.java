package vazkii.zetaimplforge.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.client.event.ContainerScreenEvent;
import vazkii.zeta.client.event.ZRenderContainerScreen;
import vazkii.zeta.event.bus.FiredAs;

@FiredAs(ZRenderContainerScreen.class)
public abstract class ForgeZRenderContainerScreen implements ZRenderContainerScreen {
	protected final ContainerScreenEvent.Render e;

	public ForgeZRenderContainerScreen(ContainerScreenEvent.Render e) {
		this.e = e;
	}

	@Override
	public AbstractContainerScreen<?> getContainerScreen() {
		return e.getContainerScreen();
	}

	@Override
	public PoseStack getPoseStack() {
		return e.getPoseStack();
	}

	@Override
	public int getMouseX() {
		return e.getMouseX();
	}

	@Override
	public int getMouseY() {
		return e.getMouseY();
	}

	@FiredAs(ZRenderContainerScreen.Foreground.class)
	public static class Foreground extends ForgeZRenderContainerScreen implements ZRenderContainerScreen.Foreground {
		public Foreground(ContainerScreenEvent.Render.Foreground e) {
			super(e);
		}
	}

	@FiredAs(ZRenderContainerScreen.Background.class)
	public static class Background extends ForgeZRenderContainerScreen implements ZRenderContainerScreen.Background {
		public Background(ContainerScreenEvent.Render.Background e) {
			super(e);
		}
	}
}
