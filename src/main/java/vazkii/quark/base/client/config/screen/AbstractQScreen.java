package vazkii.quark.base.client.config.screen;

import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import vazkii.quark.api.config.IConfigCategory;
import vazkii.quark.base.Quark;
import vazkii.quark.base.client.config.obj.AbstractStringInputObject;
import vazkii.quark.base.client.config.obj.ListObject;
import vazkii.quark.base.client.config.screen.what.DoubleInputScreen2;
import vazkii.quark.base.client.config.screen.what.IntegerInputScreen2;
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

	@Deprecated
	public OnPress categoryLink(IConfigCategory category) {
		return b -> minecraft.setScreen(new CategoryScreen(this, category));
	}

	public OnPress sectionLink(SectionDefinition section) {
		return b -> minecraft.setScreen(new SectionScreen(this, this.getChangeSet(), section));
	}

	@Deprecated
	public <T> OnPress stringInput(AbstractStringInputObject<T> object) {
		return b -> minecraft.setScreen(new StringInputScreen<>(this, object));
	}

	public OnPress stringInput2(ChangeSet changes, ValueDefinition<String> def) {
		return b -> minecraft.setScreen(new StringInputScreen2(this, changes, def));
	}

	public OnPress integerInput2(ChangeSet changes, ValueDefinition<Integer> def) {
		return b -> minecraft.setScreen(new IntegerInputScreen2(this, changes, def));
	}

	public OnPress doubleInput2(ChangeSet changes, ValueDefinition<Double> def) {
		return b -> minecraft.setScreen(new DoubleInputScreen2(this, changes, def));
	}


	public OnPress listInput(ListObject object) {
		return b -> minecraft.setScreen(new ListInputScreen(this, object));
	}

	public OnPress nothing_TODO_STUB() {
		return b -> {};
	}

	//I dunno if this should be a singleton
	private static final ChangeSet changeSet = new ChangeSet(Quark.ZETA.configInternals);
	public ChangeSet getChangeSet() {
		return changeSet;
	}
}
