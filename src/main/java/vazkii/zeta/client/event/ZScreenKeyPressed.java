package vazkii.zeta.client.event;

import net.minecraft.client.gui.screens.Screen;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZScreenKeyPressed extends IZetaPlayEvent {
	Screen getScreen();
	int getKeyCode();
	int getScanCode();
	int getModifiers();

	interface Pre extends ZScreenKeyPressed { }
	interface Post extends ZScreenKeyPressed { }
}
