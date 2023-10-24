package vazkii.zeta.config.client;

import java.util.List;

import vazkii.quark.base.client.config.screen.AbstractQScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.CheckboxButton;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class BooleanClientDefinition implements ClientDefinitionExt<ValueDefinition<Boolean>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<Boolean> def) {
		return Boolean.toString(changes.get(def));
	}

	@Override
	public void addWidgets(AbstractQScreen parent, ChangeSet changes, ValueDefinition<Boolean> def, List<WidgetWrapper> widgets) {
		widgets.add(new WidgetWrapper(new CheckboxButton(230, 3, changes, def)));
	}
}
