package org.violetmoon.quark.content.tweaks.client.screen.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.handler.MiscUtil;
import org.violetmoon.quark.content.tweaks.client.emote.EmoteDescriptor;
import org.violetmoon.quark.content.tweaks.module.EmotesModule;

public class EmoteButton extends TranslucentButton {
	public final EmoteDescriptor desc;

	public EmoteButton(int x, int y, EmoteDescriptor desc, OnPress onPress) {
		super(x, y, EmotesModule.EMOTE_BUTTON_WIDTH - 1, EmotesModule.EMOTE_BUTTON_WIDTH - 1, Component.literal(""), onPress);
		this.desc = desc;
	}

	@Override
	protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partial) {
		super.renderWidget(guiGraphics, mouseX, mouseY, partial);

		if (visible) {
			Minecraft mc = Minecraft.getInstance();
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			guiGraphics.blit(desc.texture, getX() + 4, getY() + 4, 0, 0, 16, 16, 16, 16);

			ResourceLocation tierTexture = desc.getTierTexture();
			if(tierTexture != null) {
				guiGraphics.blit(tierTexture, getX() + 4, getY() + 4, 0, 0, 16, 16, 16, 16);
			}

			boolean hovered = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + width && mouseY < getY() + height;
			if (hovered) {
				String name = desc.getLocalizedName();
				MiscUtil.Client.drawChatBubble(guiGraphics, getX(), getY(), mc.font, name, 1F, false);
			}
		}
	}
}
