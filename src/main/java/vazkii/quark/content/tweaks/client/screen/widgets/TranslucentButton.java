package vazkii.quark.content.tweaks.client.screen.widgets;

import net.minecraft.network.chat.Component;
import vazkii.quark.base.client.util.Button2;

public class TranslucentButton extends Button2 {

	public TranslucentButton(int xIn, int yIn, int widthIn, int heightIn, Component text, OnPress onPress) {
		super(xIn, yIn, widthIn, heightIn, text, onPress);
	}

	// TODO 1.19.4: gui rendering
//	@Override
//	public void blit(@Nonnull PoseStack stack, int x, int y, int textureX, int textureY, int width, int height) {
//		fill(stack, x, y, x + width, y + height, Integer.MIN_VALUE);
//	}

}
