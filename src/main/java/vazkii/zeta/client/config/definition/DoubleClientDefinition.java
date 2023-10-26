package vazkii.zeta.client.config.definition;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.client.config.screen.AbstractEditBoxInputScreen;
import vazkii.zeta.client.config.widget.PencilButton;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class DoubleClientDefinition implements ClientDefinitionExt<ValueDefinition<Double>> {
	@Override
	public String getSubtitle(ChangeSet changes, ValueDefinition<Double> def) {
		return Double.toString(changes.get(def));
	}

	@Override
	public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, ValueDefinition<Double> def, Consumer<AbstractWidget> widgets) {
		Screen newScreen = new AbstractEditBoxInputScreen<>(zc, parent, changes, def) {
			@Override
			protected int maxStringLength() {
				return 16;
			}

			@Override
			protected @Nullable Double fromString(String string) {
				try {
					return Double.parseDouble(string);
				} catch (Exception e) {
					return null;
				}
			}
		};
		widgets.accept(new PencilButton(zc, 230, 3, b -> Minecraft.getInstance().setScreen(newScreen)));
	}
}
