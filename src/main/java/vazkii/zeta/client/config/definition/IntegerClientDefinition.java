package vazkii.zeta.client.config.definition;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.client.config.screen.AbstractEditBoxInputScreen;
import vazkii.quark.base.client.config.widget.PencilButton;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class IntegerClientDefinition implements ClientDefinitionExt<ValueDefinition<Integer>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<Integer> def) {
		return Integer.toString(changes.get(def));
	}

	@Override
	public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, ValueDefinition<Integer> def, Consumer<AbstractWidget> widgets) {
		Screen newScreen = new AbstractEditBoxInputScreen<>(zc, parent, changes, def) {
			@Override
			protected @Nullable Integer fromString(String string) {
				try {
					return Integer.parseInt(string);
				} catch (Exception e) {
					return null;
				}
			}
		};
		widgets.accept(new PencilButton(230, 3, b -> Minecraft.getInstance().setScreen(newScreen)));
	}
}
