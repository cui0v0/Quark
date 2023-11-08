package org.violetmoon.zeta.event.load;

import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public interface ZAddReloadListener extends IZetaLoadEvent {
	ReloadableServerResources getServerResources();
	void addListener(PreparableReloadListener listener);
}
