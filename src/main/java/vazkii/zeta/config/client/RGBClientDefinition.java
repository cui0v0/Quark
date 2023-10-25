package vazkii.zeta.config.client;

import java.util.List;

import vazkii.quark.base.client.config.screen.AbstractQScreen;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.PencilButton;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.SectionDefinition;

public class RGBClientDefinition implements ClientDefinitionExt<SectionDefinition> {
	@Override
	public String getSubtitle(ChangeSet changes, SectionDefinition def) {
		return "rgb/a color"; //TODO
	}

	@Override
	public void addWidgets(AbstractQScreen parent, ChangeSet changes, SectionDefinition def, List<WidgetWrapper> widgets) {
		widgets.add(new WidgetWrapper(new PencilButton(230, 3, parent.rgbInput(changes, def))));
	}
}
