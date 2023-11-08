package org.violetmoon.zeta.client.event.play;

import java.util.List;

import org.violetmoon.zeta.event.bus.IZetaPlayEvent;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;

//TODO ZETA: only used for the network profiler
public interface ZCustomizeDebugText extends IZetaPlayEvent {
	List<String> getLeft();
	List<String> getRight();
	Window getWindow();
	PoseStack getPoseStack();
	float getPartialTick();
}
