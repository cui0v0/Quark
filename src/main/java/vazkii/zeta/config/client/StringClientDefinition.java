package vazkii.zeta.config.client;

import java.util.List;

import vazkii.quark.base.client.config.screen.SectionScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.PencilButton;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class StringClientDefinition implements ClientDefinitionExt<ValueDefinition<String>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<String> def) {
		return truncate(changes.get(def));
	}

	@Override
	public void addWidgets(SectionScreen parent, ChangeSet changes, ValueDefinition<String> def, List<WidgetWrapper> widgets) {
		widgets.add(new WidgetWrapper(new PencilButton(230, 3, parent.stringInput2(changes, def))));
	}
}
