package vazkii.quark.base.client.config.screen.what;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import vazkii.quark.base.QuarkClient;
import vazkii.quark.base.client.config.screen.AbstractQScreen;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.client.ClientDefinitionExt;

public abstract class AbstractSectionInputScreen extends AbstractQScreen {
	protected final ChangeSet changes;
	protected final SectionDefinition def;
	protected final ClientDefinitionExt<SectionDefinition> ext;

	protected DefaultDiscardDone defaultDiscardDone;

	public AbstractSectionInputScreen(Screen parent, ChangeSet changes, SectionDefinition def) {
		super(parent);
		this.changes = changes;
		this.def = def;
		this.ext = QuarkClient.ZETA_CLIENT.clientConfigManager.getExt(def);
	}

	protected abstract void forceUpdateWidgets();

	@Override
	protected void init() {
		super.init();

		defaultDiscardDone = new DefaultDiscardDone(this, changes, def) {
			@Override
			public void resetToDefault(Button b) {
				super.resetToDefault(b);
				forceUpdateWidgets();
			}

			@Override
			public void discard(Button b) {
				super.discard(b);
				forceUpdateWidgets();
			}
		};
		defaultDiscardDone.addWidgets(this::addRenderableWidget);

		updateButtonStatus(true);
	}

	//call changes.set() first, so isDirty will return an up-to-date value
	protected void updateButtonStatus(boolean valid) {
		defaultDiscardDone.done.active = valid;
		defaultDiscardDone.discard.active = !valid || changes.isDirty(def);
	}
}
