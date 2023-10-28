package vazkii.zeta.client.event;

import net.minecraft.client.gui.screens.Screen;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZScreenMousePressed extends IZetaPlayEvent {
	Screen getScreen();
	int getButton();

	interface Pre extends ZScreenMousePressed { }
	interface Post extends ZScreenMousePressed { }
}
