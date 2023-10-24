package vazkii.quark.base.client.config.screen.what;

import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class StringInputScreen2 extends AbstractEditBoxInputScreen<String> {
	public StringInputScreen2(Screen parent, ChangeSet changes, ValueDefinition<String> def) {
		super(parent, changes, def);
	}

	@Override
	protected @Nullable String fromString(String string) {
		return string;
	}
}
