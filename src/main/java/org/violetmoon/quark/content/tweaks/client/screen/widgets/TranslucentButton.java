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
		super.setAlpha(0.5f);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		super.setAlpha(1.0f);
	}

	@Override
	public void setFocused(boolean focused) {} // NO-OP

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	public boolean keyPressed(int i, int j, int k) {
		return false;
	}
}
