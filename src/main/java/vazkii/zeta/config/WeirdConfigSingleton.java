package vazkii.zeta.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import vazkii.quark.base.handler.GeneralConfig;
import vazkii.quark.base.module.config.ConfigFlagManager;
import vazkii.zeta.module.ZetaCategory;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.module.ZetaModuleManager;

//TODO: find somewhere better to put this, better data structure etc
public class WeirdConfigSingleton {
	private final ConfigFlagManager cfm = new ConfigFlagManager();
	private final List<Consumer<IZetaConfigInternals>> fieldUpdaters = new ArrayList<>();

	public SectionDefinition makeRootConfig(Object rootPojo, ZetaModuleManager modules) {
		SectionDefinition root = new SectionDefinition("root", List.of());

		SectionDefinition general = root.getOrCreateSubsection("general", List.of());
		ConfigObjectMapper.readInto(general, rootPojo, fieldUpdaters::add);

		for(ZetaCategory category : modules.getInhabitedCategories()) {
			//category enablement option
			ValueDefinition<Boolean> categoryEnabled = root.getOrCreateSubsection("categories", List.of()).addValue(category.name, List.of(), true);
			fieldUpdaters.add(z -> modules.MOVE_TO_CONFIG_setCategoryEnabled(category, z.get(categoryEnabled)));

			//per-category options:
			SectionDefinition categorySection = root.getOrCreateSubsection(category.name, List.of());
			for(ZetaModule module : modules.modulesInCategory(category)) {
				//module enablement option
				ValueDefinition<Boolean> moduleEnabled = categorySection.addValue(module.displayName, List.of(module.description), module.enabledByDefault);
				fieldUpdaters.add(z -> module.enabled = z.get(moduleEnabled));

				//module @Config options
				SectionDefinition moduleSection = categorySection.getOrCreateSubsection(module.lowercaseName, List.of(module.description));
				ConfigObjectMapper.readInto(moduleSection, module, fieldUpdaters::add);

				//anti overlap
				if(!module.antiOverlap.isEmpty()) {
					StringBuilder desc = new StringBuilder("This feature disables itself if any of the following mods are loaded: \n");
					for (String s : module.antiOverlap)
						desc.append(" - ").append(s).append("\n");
					desc.append("This is done to prevent content overlap.\nYou can turn this on to force the feature to be loaded even if the above mods are also loaded.");

					ValueDefinition<Boolean> ignoreAntiOverlap = moduleSection.addValue("Ignore Anti Overlap", List.of(desc.toString()), false);
					fieldUpdaters.add(z -> module.ignoreAntiOverlap = !GeneralConfig.useAntiOverlap || z.get(ignoreAntiOverlap));
				}
			}
		}

		return root;
	}

	public ConfigFlagManager getConfigFlagManager() {
		return cfm;
	}

	public void onReload(IZetaConfigInternals internals) {
		fieldUpdaters.forEach(c -> c.accept(internals));
	}
}
