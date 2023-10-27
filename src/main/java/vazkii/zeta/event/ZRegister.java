package vazkii.zeta.event;

import vazkii.zeta.Zeta;
import vazkii.zeta.event.bus.IZetaLoadEvent;
import vazkii.zeta.registry.CraftingExtensionsRegistry;
import vazkii.zeta.registry.ZetaRegistry;

@SuppressWarnings("ClassCanBeRecord")
public class ZRegister implements IZetaLoadEvent {
	public final Zeta zeta;

	public ZRegister(Zeta zeta) {
		this.zeta = zeta;
	}

	public ZetaRegistry getRegistry() {
		return zeta.registry;
	}

	public CraftingExtensionsRegistry getCraftingExtensionsRegistry() {
		return zeta.craftingExtensions;
	}

	public static class Post implements IZetaLoadEvent { }
}
