package vazkii.quark.base.client.config.screen.what;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import vazkii.quark.base.QuarkClient;
import vazkii.quark.base.client.config.screen.AbstractQScreen;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;
import vazkii.zeta.config.client.ClientDefinitionExt;

public abstract class AbstractInputScreen2<T> extends AbstractQScreen {
	protected final ChangeSet changes;
	protected final ValueDefinition<T> def;
	protected final ClientDefinitionExt<ValueDefinition<T>> ext;

	protected DefaultDiscardDone defaultDiscardDone;

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

		defaultDiscardDone = new DefaultDiscardDone(this, changes, def) {
			@Override
			public void resetToDefault(Button b) {
				super.resetToDefault(b);
				setTo(changes.get(def));
			}

			@Override
			public void discard(Button b) {
				super.discard(b);
				setTo(changes.get(def));
			}
		};
		defaultDiscardDone.addWidgets(this::addRenderableWidget);

		updateButtonStatus(true);
	}

	protected abstract void setTo(T value);

	//call changes.set() first, so isDirty will return an up-to-date value
	protected void updateButtonStatus(boolean valid) {
		defaultDiscardDone.done.active = valid;
		defaultDiscardDone.discard.active = !valid || changes.isDirty(def);
	}
}
