package org.violetmoon.quark.content.tweaks.client.screen.widgets;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

public class TranslucentButton extends Button {
	public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, Component text, OnPress onPress) {
		super(new Button.Builder(text, onPress).size(widthIn, heightIn).pos(xIn, yIn));
	}

	@Override
	public void blit(@NotNull PoseStack stack, int x, int y, int textureX, int textureY, int width, int height) {
		fill(stack, x, y, x + width, y + height, Integer.MIN_VALUE);
	}
}
