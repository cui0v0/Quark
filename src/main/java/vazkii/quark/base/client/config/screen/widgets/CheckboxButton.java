package vazkii.quark.base.client.config.screen.widgets;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import vazkii.quark.base.handler.MiscUtil;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class CheckboxButton extends Button {

	private final ValueDefinition<Boolean> value;
	private final ChangeSet changes;

	public CheckboxButton(int x, int y, ChangeSet changes, ValueDefinition<Boolean> value) {
		super(x, y, 20, 20, Component.literal(""), CheckboxButton::toggle);
		this.value = value;
		this.changes = changes;
	}

	private static void toggle(Button press) {
		if(press instanceof CheckboxButton checkbox) {
			checkbox.changes.toggle(checkbox.value);
		}
	}

	@Override
	public void renderButton(@Nonnull PoseStack mstack, int mouseX, int mouseY, float partial) {
		super.renderButton(mstack, mouseX, mouseY, partial);

		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, MiscUtil.GENERAL_ICONS);
		boolean enabled = changes.get(value) && active;
		int u = enabled ? 0 : 16;
		int v = 93;

		blit(mstack, x + 2, y + 1, u, v, 15, 15);
	}

}
