package vazkii.quark.base.client.config.screen.what;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import vazkii.quark.base.QuarkClient;
import vazkii.quark.base.client.config.screen.AbstractQScreen;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;
import vazkii.zeta.config.client.ClientDefinitionExt;

public abstract class AbstractInputScreen2<T> extends AbstractQScreen {
	protected final ChangeSet changes;
	protected final ValueDefinition<T> def;
	protected final ClientDefinitionExt<ValueDefinition<T>> ext;

	protected Button resetButton, doneButton;

	public AbstractInputScreen2(Screen parent, ChangeSet changes, ValueDefinition<T> def) {
		super(parent);
		this.changes = changes;
		this.def = def;
		this.ext = QuarkClient.ZETA_CLIENT.clientConfigManager.getExt(def);
	}

	@Override
	public void render(@Nonnull PoseStack mstack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(mstack);

		super.render(mstack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void init() {
		super.init();

		int pad = 3;
		int bWidth = 121;
		int left = (width - (bWidth + pad) * 3) / 2;
		int vStart = height - 30;

		addRenderableWidget(new Button(left, vStart, bWidth, 20, Component.translatable("quark.gui.config.default"), this::setDefault));
		resetButton = addRenderableWidget(new Button(left + bWidth + pad, vStart, bWidth, 20, Component.translatable("quark.gui.config.discard"), this::discardChanges));
		doneButton = addRenderableWidget(new Button(left + (bWidth + pad) * 2, vStart, bWidth, 20, Component.translatable("gui.done"), this::returnToParent));

		updateButtonStatus(true);
	}

	protected void setDefault(Button click) {
		changes.resetToDefault(def);
		setTo(changes.get(def));
	}

	protected void discardChanges(Button click) {
		changes.removeChange(def);
		setTo(changes.get(def));
	}

	protected abstract void setTo(T value);

	protected final boolean isDirty() {
		return changes.isDirty(def);
	}

	//call changes.set() first, so isDirty will return an up-to-date value
	protected void updateButtonStatus(boolean valid) {
		doneButton.active = valid;
		resetButton.active = !valid || isDirty();
	}
}
