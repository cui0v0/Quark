package vazkii.zeta.config.client;

import java.util.List;

import vazkii.quark.base.client.config.screen.SectionScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.PencilButton;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class IntegerClientDefinition implements ClientDefinitionExt<ValueDefinition<Integer>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<Integer> def) {
		return Integer.toString(changes.get(def));
	}

	@Override
	public void addWidgets(SectionScreen parent, ChangeSet changes, ValueDefinition<Integer> def, List<WidgetWrapper> widgets) {
		widgets.add(new WidgetWrapper(new PencilButton(230, 3, parent.integerInput2(changes, def))));
	}
}
