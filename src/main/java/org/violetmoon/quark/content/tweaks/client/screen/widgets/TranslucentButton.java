package org.violetmoon.quark.content.tweaks.client.screen.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TranslucentButton extends Button {
	public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, Component text, OnPress onPress) {
		super(new Button.Builder(text, onPress).size(widthIn, heightIn).pos(xIn, yIn));
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		// FIXME VERY VERY VERY BROKEN!!!!
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		//guiGraphics.fill(mouseX, mouseY, 75, 20, Integer.MIN_VALUE);
	}
}
