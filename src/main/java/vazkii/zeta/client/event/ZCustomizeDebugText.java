package vazkii.zeta.client.event;

import java.util.List;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import vazkii.zeta.event.bus.IZetaPlayEvent;

//TODO ZETA: only used for the network profiler
public interface ZCustomizeDebugText extends IZetaPlayEvent {
	List<String> getLeft();
	List<String> getRight();
	Window getWindow();
	PoseStack getPoseStack();
	float getPartialTick();
}
