package vazkii.zeta.client.event;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderOverlay extends IZetaPlayEvent {
	Window getWindow();
	PoseStack getPoseStack();

	interface Chat extends ZRenderOverlay, IZetaPlayEvent {
		interface Pre extends Chat, IZetaPlayEvent { }
		interface Post extends Chat, IZetaPlayEvent { }
	}
	interface Crosshair extends ZRenderOverlay, IZetaPlayEvent { }
	interface Hotbar extends ZRenderOverlay, IZetaPlayEvent { }
}
