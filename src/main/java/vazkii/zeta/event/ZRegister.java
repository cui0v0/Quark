package vazkii.zeta.event;

import vazkii.zeta.event.bus.IZetaLoadEvent;
import vazkii.zeta.registry.ZetaRegistry;

@SuppressWarnings("ClassCanBeRecord")
public class ZRegister implements IZetaLoadEvent {
	public final ZetaRegistry registry;

	public ZRegister(ZetaRegistry registry) {
		this.registry = registry;
	}

	public ZetaRegistry getRegistry() {
		return registry;
	}

	public static class Post implements IZetaLoadEvent { }
}
