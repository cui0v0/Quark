package vazkii.zeta.client.event;

import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZClientSetup extends IZetaLoadEvent {
	void enqueueWork(Runnable run);
}
