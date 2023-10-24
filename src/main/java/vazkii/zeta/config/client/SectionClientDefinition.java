package vazkii.zeta.config.client;

import java.util.List;

import net.minecraft.client.resources.language.I18n;
import vazkii.quark.base.client.config.screen.AbstractQScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.PencilButton;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.SectionDefinition;

public class SectionClientDefinition implements ClientDefinitionExt<SectionDefinition> {
	@Override
	public String getSubtitle(ChangeSet changes, SectionDefinition def) {
		//TODO: expose both numbers in the UI, not just one (needs more strings)
		int size = def.getSubsections().size() + def.getValues().size();
		return (size == 1 ? I18n.get("quark.gui.config.onechild") : I18n.get("quark.gui.config.nchildren", size));
	}

	@Override
	public void addWidgets(AbstractQScreen parent, ChangeSet changes, SectionDefinition def, List<WidgetWrapper> widgets) {
		widgets.add(new WidgetWrapper(new PencilButton(230, 3, parent.sectionLink(changes, def))));
	}
}
