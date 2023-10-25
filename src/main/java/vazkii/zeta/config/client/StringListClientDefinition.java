package vazkii.zeta.config.client;

import java.util.List;

import vazkii.quark.base.client.config.screen.AbstractQScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.PencilButton;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class StringListClientDefinition implements ClientDefinitionExt<ValueDefinition<List<String>>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<List<String>> def) {
		List<String> list = changes.get(def);

		if(list.isEmpty())
			return "[]";

		StringBuilder bob = new StringBuilder("[").append(list.get(0));
		for(int i = 1; i < list.size() && bob.length() < 30; i++)
			bob.append(", ").append(list.get(i));

		if(bob.length() > 30)
			return truncate(bob.toString());
		else
			return bob.append(']').toString();
	}

	@Override
	public void addWidgets(AbstractQScreen parent, ChangeSet changes, ValueDefinition<List<String>> def, List<WidgetWrapper> widgets) {
		widgets.add(new WidgetWrapper(new PencilButton(230, 3, parent.stringListInput(changes, def))));
	}
}
