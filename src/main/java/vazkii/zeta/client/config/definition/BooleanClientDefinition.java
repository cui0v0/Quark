package vazkii.zeta.client.config.definition;

import java.util.function.Consumer;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import vazkii.zeta.client.config.widget.CheckboxButton;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class BooleanClientDefinition implements ClientDefinitionExt<ValueDefinition<Boolean>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<Boolean> def) {
		return Boolean.toString(changes.get(def));
	}

	@Override
	public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, ValueDefinition<Boolean> def, Consumer<AbstractWidget> widgets) {
		widgets.accept(new CheckboxButton(zc, 230, 3, changes, def));
	}
}
