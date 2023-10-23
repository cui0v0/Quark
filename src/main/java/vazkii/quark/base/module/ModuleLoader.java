package vazkii.quark.base.module;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import vazkii.quark.base.Quark;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.item.IQuarkItem;
import vazkii.quark.base.module.config.ConfigFlagManager;
import vazkii.zeta.event.ZRegister;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.module.ZetaModule;

//TODO ZETA: im in the process of stripping this class for parts
@Deprecated
public final class ModuleLoader {

	public static final ModuleLoader INSTANCE = new ModuleLoader();

	//private ConfigResolver config;
	public Runnable onConfigReloadJEI;

	private ModuleLoader() { }

	public void start() {
		//config = new ConfigResolver();
		//config.makeSpec();
	}

	public ConfigFlagManager getConfigFlagManager() {
		return Quark.ZETA.weirdConfigSingleton.getConfigFlagManager();
	}

	@LoadEvent
	public void register(ZRegister event) {
	}

	public void configChanged() {
		//if (onConfigReloadJEI != null)
		//	onConfigReloadJEI.run();
		//config.configChanged();
	}

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

	//and this
	public boolean isItemEnabled(Item i) {
		if(i instanceof IQuarkItem qi) {
			return qi.isEnabled();
		}
		else if(i instanceof BlockItem bi) {
			Block b = bi.getBlock();
			if(b instanceof IQuarkBlock qb) {
				return qb.isEnabled();
			}
		}

		return true;
	}

	/**
	 * Meant only to be called internally.
	 */
	public void initJEICompat(Runnable jeiRunnable) {
		onConfigReloadJEI = jeiRunnable;
		onConfigReloadJEI.run();
	}

}
