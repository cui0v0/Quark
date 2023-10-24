package vazkii.zeta.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import vazkii.quark.base.handler.GeneralConfig;
import vazkii.quark.base.module.config.ConfigFlagManager;
import vazkii.zeta.Zeta;
import vazkii.zeta.module.ZetaCategory;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.module.ZetaModuleManager;

//TODO: find somewhere better to put this, better data structure etc
public class WeirdConfigSingleton {
	private final Zeta z;
	private final ConfigFlagManager cfm = new ConfigFlagManager();
	private final SectionDefinition rootConfig;

	//for updating the values of @Config annotations to match the current state of the config
	private final List<Consumer<IZetaConfigInternals>> fieldUpdaters = new ArrayList<>();

	//ummmmmmm i think my abstraction isn't very good
	private final SectionDefinition generalSection;
	private final Map<ZetaCategory, SectionDefinition> categoriesToSections = new HashMap<>();
	private final Map<ZetaCategory, ValueDefinition<Boolean>> categoryEnabledOptions = new HashMap<>();
	private final Map<ZetaModule, ValueDefinition<Boolean>> moduleEnabledOptions = new HashMap<>();

	//state (TODO: unused)
	private final Set<ZetaCategory> enabledCategories = new HashSet<>();

	public WeirdConfigSingleton(Zeta z, Object rootPojo) {
		this.z = z;

		ZetaModuleManager modules = z.modules;

		//all modules are enabled by default
		enabledCategories.addAll(modules.getCategories());
		//TODO: track module enablement too with a hashset too?

		this.rootConfig = new SectionDefinition("root", List.of());

		if(rootPojo == null)
			generalSection = null;
		else {
			generalSection = rootConfig.getOrCreateSubsection("general", List.of());
			ConfigObjectMapper.readInto(generalSection, rootPojo, fieldUpdaters::add);
		}

		for(ZetaCategory category : modules.getInhabitedCategories()) {
			//category enablement option
			//TODO: why aren't these showing up?
			ValueDefinition<Boolean> categoryEnabled = rootConfig.getOrCreateSubsection("categories", List.of("cateogyr enabled :O")).addValue(category.name, List.of("is it enabed"), true);
			categoryEnabledOptions.put(category, categoryEnabled);
			fieldUpdaters.add(i -> setCategoryEnabled(category, i.get(categoryEnabled)));

			//per-category options:
			SectionDefinition categorySection = rootConfig.getOrCreateSubsection(category.name, List.of());
			categoriesToSections.put(category, categorySection);

			for(ZetaModule module : modules.modulesInCategory(category)) {
				//module enablement option
				ValueDefinition<Boolean> moduleEnabled = categorySection.addValue(module.displayName, List.of(module.description), module.enabledByDefault);
				moduleEnabledOptions.put(module, moduleEnabled);
				fieldUpdaters.add(i -> setModuleEnabled(module, i.get(moduleEnabled)));

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
					fieldUpdaters.add(i -> module.ignoreAntiOverlap = !GeneralConfig.useAntiOverlap || i.get(ignoreAntiOverlap));
				}
			}
		}
	}

	public SectionDefinition getRootConfig() {
		return rootConfig;
	}

	// bad bad bad

	public SectionDefinition getGeneralSection() {
		return generalSection;
	}

	public SectionDefinition getCategorySection(ZetaCategory cat) {
		return categoriesToSections.get(cat);
	}

	public ValueDefinition<Boolean> getCategoryEnabledOption(ZetaCategory cat) {
		return categoryEnabledOptions.get(cat);
	}

	public ValueDefinition<Boolean> getModuleEnabledOption(ZetaModule module) {
		return moduleEnabledOptions.get(module);
	}

	// support for the options added by this class

	private void setCategoryEnabled(ZetaCategory cat, boolean enabled) {
		if(enabled)
			enabledCategories.add(cat);
		else
			enabledCategories.remove(cat);

		//TODO TODO bad bad bad
		for(ZetaModule mod : z.modules.modulesInCategory(cat)) {
			mod.setEnabled(z, mod.enabled);
		}
	}

	private void setModuleEnabled(ZetaModule module, boolean enabled) {
		module.setEnabled(z, enabled);
	}

	public boolean isCategoryEnabled(ZetaCategory cat) {
		return enabledCategories.contains(cat);
	}

	//ummm

	public ConfigFlagManager getConfigFlagManager() {
		return cfm;
	}

	public void onReload(IZetaConfigInternals internals) {
		fieldUpdaters.forEach(c -> c.accept(internals));
	}
}
