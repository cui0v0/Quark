package vazkii.quark.base.client.config.screen;

import java.util.List;

import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import vazkii.quark.api.config.IConfigCategory;
import vazkii.quark.base.client.config.obj.AbstractStringInputObject;
import vazkii.quark.base.client.config.obj.ListObject;
import vazkii.quark.base.client.config.screen.what.DoubleInputScreen;
import vazkii.quark.base.client.config.screen.what.IntegerInputScreen;
import vazkii.quark.base.client.config.screen.what.StringListInputScreen;
import vazkii.quark.base.client.config.screen.what.SectionScreen;
import vazkii.quark.base.client.config.screen.what.StringInputScreen2;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.ValueDefinition;

public abstract class AbstractQScreen extends Screen {

	private final Screen parent;

	public AbstractQScreen(Screen parent) {
		super(Component.literal(""));
		this.parent = parent;
	}

	public void returnToParent(Button button) {
		minecraft.setScreen(parent);
	}

	public OnPress webLink(String url) {
		return b -> Util.getPlatform().openUri(url);
	}

	public OnPress sectionLink(ChangeSet changes, SectionDefinition section) {
		return b -> minecraft.setScreen(new SectionScreen(this, changes, section));
	}

	public OnPress stringInput2(ChangeSet changes, ValueDefinition<String> def) {
		return b -> minecraft.setScreen(new StringInputScreen2(this, changes, def));
	}

	public OnPress integerInput(ChangeSet changes, ValueDefinition<Integer> def) {
		return b -> minecraft.setScreen(new IntegerInputScreen(this, changes, def));
	}

	public OnPress doubleInput(ChangeSet changes, ValueDefinition<Double> def) {
		return b -> minecraft.setScreen(new DoubleInputScreen(this, changes, def));
	}

	public OnPress stringListInput(ChangeSet changes, ValueDefinition<List<String>> def) {
		return b -> minecraft.setScreen(new StringListInputScreen(this, changes, def));
	}

	@Deprecated
	public OnPress categoryLink(IConfigCategory category) {
		return b -> minecraft.setScreen(new CategoryScreen(this, category));
	}

	@Deprecated
	public <T> OnPress stringInput(AbstractStringInputObject<T> object) {
		return b -> minecraft.setScreen(new StringInputScreen<>(this, object));
	}

	@Deprecated
	public OnPress listInput(ListObject object) {
		return b -> minecraft.setScreen(new ListInputScreen(this, object));
	}
}
