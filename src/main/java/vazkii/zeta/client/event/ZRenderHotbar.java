package vazkii.zeta.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import vazkii.zeta.event.bus.Cancellable;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderHotbar extends IZetaPlayEvent {
	PoseStack getPoseStack();

	interface Pre extends ZRenderHotbar, Cancellable { }
	interface Post extends ZRenderHotbar { }
}
