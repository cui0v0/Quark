package vazkii.zeta.event;

import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZAddReloadListener extends IZetaLoadEvent {
	ReloadableServerResources getServerResources();
	void addListener(PreparableReloadListener listener);
}
