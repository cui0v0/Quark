package vazkii.quark.base.module;

import vazkii.quark.base.Quark;
import vazkii.zeta.module.ZetaModule;

//TODO ZETA: im in the process of stripping this class for parts
@Deprecated
public final class ModuleLoader {

	public static final ModuleLoader INSTANCE = new ModuleLoader();

	private ModuleLoader() { }

	//tempting to push this method directly through to Quark.ZETA.modules...
	//but i think it's more appropriate to have this stored in a configuration class
	@Deprecated
	public boolean isModuleEnabled(Class<? extends ZetaModule> moduleClazz) {
		ZetaModule module = Quark.ZETA.modules.get(moduleClazz);
		return module != null && module.enabled;
	}

	//same for this
	@Deprecated
	public boolean isModuleEnabledOrOverlapping(Class<? extends ZetaModule> moduleClazz) {
		ZetaModule module = Quark.ZETA.modules.get(moduleClazz);
		return module != null && (module.enabled || module.disabledByOverlap);
	}

}
