package vazkii.quark.base.client.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.quark.api.config.IConfigCategory;
import vazkii.quark.api.config.IConfigElement;
import vazkii.quark.api.config.IConfigObject;
import vazkii.quark.base.Quark;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.module.ZetaCategory;

import java.util.LinkedHashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
@Deprecated
public final class IngameConfigHandler {

	@Deprecated
	public static final IngameConfigHandler INSTANCE = new IngameConfigHandler();

	//TODO: make this non-static mayybe
	public final ChangeSet changeSet = new ChangeSet(Quark.ZETA.configInternals);

	public Map<String, TopLevelCategory> topLevelCategories = new LinkedHashMap<>();

	private IngameConfigHandler() {}

	public IConfigObject<Boolean> getCategoryEnabledObject(ZetaCategory category) {
		return topLevelCategories.get("categories").getModuleOption(category);
	}

	public IConfigCategory getConfigCategory(ZetaCategory category) {
		return topLevelCategories.get(category == null ? "general" : category.name);
	}

	public void refresh() {
		topLevelCategories.values().forEach(IConfigElement::refresh);
	}

	public void commit() {
		Quark.proxy.setConfigGuiSaving(true);
		try {
			commit(topLevelCategories);
			Quark.proxy.handleQuarkConfigChange();
		} finally {
			Quark.proxy.setConfigGuiSaving(false);
		}
	}

	public static <T extends IConfigCategory> void commit(Map<String, T> map) {
		for(IConfigCategory c : map.values()) {
			if(c.isDirty()) {
				c.save();
				c.clean();
			}
		}
	}

}
