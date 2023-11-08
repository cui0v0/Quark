package org.violetmoon.quark.content.tweaks.module;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.module.config.Config;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "tweaks")
public class SpongesBetterModule extends ZetaModule {

	@Config(description = "The maximum number of water tiles that a sponge can soak up. Vanilla default is 64.")
	@Config.Min(64)
	public static int maximumWaterDrain = 256;

	@Config(description = "The maximum number of water tiles that a sponge can 'crawl along' for draining. Vanilla default is 6.")
	@Config.Min(6)
	public static int maximumCrawlDistance = 10;

	public static int drainLimit(int previous) {
		if (Quark.ZETA.modules.isEnabled(SpongesBetterModule.class)) {
			// Additive to not directly conflict with other mods
			return maximumWaterDrain - 64 + previous;
		}
		return previous;
	}

	public static int crawlLimit(int previous) {
		if (Quark.ZETA.modules.isEnabled(SpongesBetterModule.class)) {
			// Additive to not directly conflict with other mods
			return maximumCrawlDistance - 6 + previous;
		}
		return previous;
	}

}
