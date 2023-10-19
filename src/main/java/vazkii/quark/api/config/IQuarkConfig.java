package vazkii.quark.api.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IQuarkConfig {

	class Holder {

		/**
		 * Access this only after construct. clientSetup is safe.
		 */
		public static IQuarkConfig instance = null;

	}
}
