package vazkii.zeta.client.config.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import vazkii.zeta.client.config.widget.DefaultDiscardDone;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.client.config.definition.ClientDefinitionExt;

public abstract class AbstractSectionInputScreen extends ZetaScreen {
	protected final ChangeSet changes;
	protected final SectionDefinition def;
	protected final ClientDefinitionExt<SectionDefinition> ext;

	protected DefaultDiscardDone defaultDiscardDone;

	public AbstractSectionInputScreen(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition def) {
		super(zc, parent);
		this.changes = changes;
		this.def = def;
		this.ext = zc.clientConfigManager.getExt(def);
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
