package vazkii.zeta.client.event;

import net.minecraft.client.KeyMapping;
import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZKeyMapping extends IZetaLoadEvent {
	void register(KeyMapping key);
}
