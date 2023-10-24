package vazkii.zeta.config.client;

import java.util.List;

import vazkii.quark.base.client.config.screen.SectionScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.PencilButton;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class DoubleClientDefinition implements ClientDefinitionExt<ValueDefinition<Double>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<Double> def) {
		return Double.toString(changes.get(def));
	}

	@Override
	public void addWidgets(SectionScreen parent, ChangeSet changes, ValueDefinition<Double> def, List<WidgetWrapper> widgets) {
		widgets.add(new WidgetWrapper(new PencilButton(230, 3, parent.doubleInput2(changes, def))));
	}
}
