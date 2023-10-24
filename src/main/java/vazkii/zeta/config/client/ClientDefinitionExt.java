package vazkii.zeta.config.client;

import java.util.List;

import net.minecraft.client.resources.language.I18n;
import vazkii.quark.base.client.config.screen.SectionScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.Definition;
import vazkii.zeta.config.SectionDefinition;

public interface ClientDefinitionExt<T extends Definition> {
	default String getGuiDisplayName(ChangeSet changes, T def) {
		String defName = def instanceof SectionDefinition ? def.name.replace("_", "") : def.name;
		String transKey = "quark.config." + String.join(".", def.path) + "." + def.name.toLowerCase().replaceAll(" ", "_").replaceAll("[^A-Za-z0-9_]", "") + ".name";

		String localized = I18n.get(transKey);
		if(localized.isEmpty() || localized.equals(transKey))
			return defName;

		return localized;
	}

	String getSubtitle(ChangeSet changes, T def);

	void addWidgets(SectionScreen parent, ChangeSet changes, T def, List<WidgetWrapper> widgets);

	default String truncate(String in) {
		if(in.length() > 30)
			return in.substring(27) + "...";
		else
			return in;
	}

	class Default implements ClientDefinitionExt<Definition> {
		@Override
		public String getSubtitle(ChangeSet changes, Definition def) {
			return "";
		}

		@Override
		public void addWidgets(SectionScreen parent, ChangeSet changes, Definition def, List<WidgetWrapper> widgets) {

		}
	}
}
