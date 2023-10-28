package vazkii.zeta.client.event;

import net.minecraft.client.gui.screens.Screen;
import vazkii.zeta.event.bus.Cancellable;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZScreenCharacterTyped extends IZetaPlayEvent, Cancellable {
	Screen getScreen();
	char getCodePoint();
	int getModifiers();

	interface Pre extends ZScreenCharacterTyped { }
	interface Post extends ZScreenCharacterTyped { }
}
