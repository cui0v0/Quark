package vazkii.quark.base.client.config.screen.what;

import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class DoubleInputScreen extends AbstractEditBoxInputScreen<Double> {
	public DoubleInputScreen(Screen parent, ChangeSet changes, ValueDefinition<Double> def) {
		super(parent, changes, def);
	}

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
}
