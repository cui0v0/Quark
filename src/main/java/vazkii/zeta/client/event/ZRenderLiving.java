package vazkii.zeta.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZRenderLiving extends IZetaPlayEvent {
	Entity getEntity();
	PoseStack getPoseStack();

	//ugly consequence of zeta's lackluster handling of event priorities, and me not wanting a combinatorial explosion
	interface PreHighest extends ZRenderLiving { }
	interface PostLowest extends ZRenderLiving { }
}
