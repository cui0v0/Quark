package org.violetmoon.zeta.client.config.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.client.ZetaClient;

import java.util.List;

public class CategoryButton extends Button {
	private final ZetaClient zc;
	private final ItemStack icon;
	private final Component text;

	public CategoryButton(ZetaClient zc, int x, int y, int w, int h, Component text, ItemStack icon, OnPress onClick) {
		//fixme make sure this works
		//super(x, y, w, h, Component.literal(""), onClick);
		super(new Button.Builder(Component.literal(""), onClick).pos(x, y).size(w, h));
		this.zc = zc;
		this.icon = icon;
		this.text = text;
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.render(guiGraphics, mouseX, mouseY, partialTicks);

		if(!active && isHovered)
			zc.topLayerTooltipHandler.setTooltip(List.of(I18n.get("quark.gui.config.missingaddon")), mouseX, mouseY);

		Minecraft mc = Minecraft.getInstance();
		guiGraphics.renderFakeItem(icon, getX() + 5, getY() + 2);

		int iconPad = (16 + 5) / 2;
		guiGraphics.drawCenteredString(mc.font, text, getX() + width / 2 + iconPad, getY() + (height - 8) / 2, getFGColor());
	}
}
