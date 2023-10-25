package vazkii.quark.base.module.config.type;

import vazkii.zeta.module.ZetaModule;
import vazkii.quark.base.module.config.ConfigFlagManager;

public interface IConfigType {

	default void onReload(ZetaModule module, ConfigFlagManager flagManager) { }
	
}
