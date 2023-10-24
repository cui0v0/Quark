package vazkii.quark.base.client.config.screen;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import vazkii.quark.api.config.IConfigCategory;
import vazkii.quark.base.client.config.IngameConfigHandler;
import vazkii.quark.base.client.config.obj.AbstractStringInputObject;
import vazkii.quark.base.client.config.obj.ListObject;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.SectionDefinition;

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

	public OnPress categoryLink(IConfigCategory category) {
		return b -> minecraft.setScreen(new CategoryScreen(this, category));
	}

	public OnPress sectionLink(SectionDefinition section) {
		return b -> minecraft.setScreen(new SectionScreen(this, section));
	}

	public <T> OnPress stringInput(AbstractStringInputObject<T> object) {
		return b -> minecraft.setScreen(new StringInputScreen<>(this, object));
	}

	public OnPress listInput(ListObject object) {
		return b -> minecraft.setScreen(new ListInputScreen(this, object));
	}

	public OnPress nothing_TODO_STUB() {
		return b -> {};
	}

	//it's recommended to access it through here for now, since I dunno if this should be a singleton
	public ChangeSet getChangeSet() {
		return IngameConfigHandler.INSTANCE.changeSet;
	}
}
