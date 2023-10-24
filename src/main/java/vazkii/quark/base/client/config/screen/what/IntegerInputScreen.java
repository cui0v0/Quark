package vazkii.quark.base.client.config.screen.what;

import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class IntegerInputScreen extends AbstractEditBoxInputScreen<Integer> {
	public IntegerInputScreen(Screen parent, ChangeSet changes, ValueDefinition<Integer> valueDef) {
		super(parent, changes, valueDef);
	}

	@Override
	protected @Nullable Integer fromString(String string) {
		try {
			return Integer.parseInt(string);
		} catch (Exception e) {
			return null;
		}
	}
}
