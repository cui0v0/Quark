package vazkii.zeta.client.event;

import java.util.List;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZScreenInit extends IZetaPlayEvent {
	Screen getScreen();
	List<GuiEventListener> getListenersList();
	void addListener(GuiEventListener listener);
	void removeListener(GuiEventListener listener);

	interface Pre extends ZScreenInit { }
	interface Post extends ZScreenInit { }
}
