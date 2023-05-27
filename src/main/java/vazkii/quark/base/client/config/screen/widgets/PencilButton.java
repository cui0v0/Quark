package vazkii.quark.base.client.config.screen.widgets;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import vazkii.quark.base.client.util.Button2;
import vazkii.quark.base.handler.MiscUtil;

public class PencilButton extends Button2 {

	public PencilButton(int x, int y, OnPress pressable) {
		super(x, y, 20, 20, Component.literal(""), pressable);
	}

	@Override
	public void renderWidget(@Nonnull PoseStack mstack, int mouseX, int mouseY, float partialTicks) {
		super.renderWidget(mstack, mouseX, mouseY, partialTicks);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, MiscUtil.GENERAL_ICONS);

		int u = 32;
		int v = 93;

		blit(mstack, getX() + 2, getY() + 1, u, v, 16, 16);
	}

}
