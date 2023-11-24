package org.violetmoon.quark.content.tweaks.client.screen.widgets;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class TranslucentButton extends Button {
	public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, Component text, OnPress onPress) {
		super(new Button.Builder(text, onPress).size(widthIn, heightIn).pos(xIn, yIn));
	}
}
