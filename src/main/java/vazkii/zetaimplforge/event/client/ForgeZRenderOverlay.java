package vazkii.zetaimplforge.event.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import vazkii.zeta.client.event.ZRenderOverlay;
import vazkii.zeta.event.bus.FiredAs;

@FiredAs(ZRenderOverlay.class)
public class ForgeZRenderOverlay implements ZRenderOverlay {
	private final RenderGuiOverlayEvent e;

	public ForgeZRenderOverlay(RenderGuiOverlayEvent e) {
		this.e = e;
	}

	@Override
	public Window getWindow() {
		return e.getWindow();
	}

	@Override
	public PoseStack getPoseStack() {
		return e.getPoseStack();
	}

	@Override
	public boolean shouldDrawSurvivalElements() {
		return Minecraft.getInstance().gui instanceof ForgeGui fgui && fgui.shouldDrawSurvivalElements();
	}

	@Override
	public int getLeftHeight() {
		return Minecraft.getInstance().gui instanceof ForgeGui fgui ? fgui.leftHeight : 39;
	}

	@FiredAs(ZRenderOverlay.ArmorLevel.class)
	public static class ArmorLevel extends ForgeZRenderOverlay implements ZRenderOverlay.ArmorLevel {
		public ArmorLevel(RenderGuiOverlayEvent e) {
			super(e);
		}

		@FiredAs(ZRenderOverlay.ArmorLevel.Pre.class)
		public static class Pre extends ForgeZRenderOverlay.ArmorLevel implements ZRenderOverlay.ArmorLevel.Pre {
			public Pre(RenderGuiOverlayEvent.Pre e) {
				super(e);
			}
		}

		@FiredAs(ZRenderOverlay.ArmorLevel.Post.class)
		public static class Post extends ForgeZRenderOverlay.ArmorLevel implements ZRenderOverlay.ArmorLevel.Post {
			public Post(RenderGuiOverlayEvent.Post e) {
				super(e);
			}
		}
	}

	@FiredAs(ZRenderOverlay.Chat.class)
	public static class Chat extends ForgeZRenderOverlay implements ZRenderOverlay.Chat {
		public Chat(RenderGuiOverlayEvent e) {
			super(e);
		}

		@FiredAs(ZRenderOverlay.Chat.Pre.class)
		public static class Pre extends ForgeZRenderOverlay.Chat implements ZRenderOverlay.Chat.Pre {
			public Pre(RenderGuiOverlayEvent.Pre e) {
				super(e);
			}
		}

		@FiredAs(ZRenderOverlay.Chat.Post.class)
		public static class Post extends ForgeZRenderOverlay.Chat implements ZRenderOverlay.Chat.Post {
			public Post(RenderGuiOverlayEvent.Post e) {
				super(e);
			}
		}
	}

	@FiredAs(ZRenderOverlay.Crosshair.class)
	public static class Crosshair extends ForgeZRenderOverlay implements ZRenderOverlay.Crosshair {
		public Crosshair(RenderGuiOverlayEvent e) {
			super(e);
		}
	}

	@FiredAs(ZRenderOverlay.Hotbar.class)
	public static class Hotbar extends ForgeZRenderOverlay implements ZRenderOverlay.Hotbar {
		public Hotbar(RenderGuiOverlayEvent e) {
			super(e);
		}
	}
}
