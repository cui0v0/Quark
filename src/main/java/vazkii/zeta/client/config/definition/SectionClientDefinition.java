package vazkii.zeta.client.config.definition;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import vazkii.zeta.client.config.screen.SectionScreen;
import vazkii.zeta.client.config.widget.PencilButton;
import vazkii.zeta.client.ZetaClient;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.SectionDefinition;

public class SectionClientDefinition implements ClientDefinitionExt<SectionDefinition> {
	@Override
	public String getSubtitle(ChangeSet changes, SectionDefinition def) {
		//TODO: expose both numbers in the UI, not just one (needs more strings)
		int size = def.getSubsections().size() + def.getValues().size();
		return (size == 1 ? I18n.get("quark.gui.config.onechild") : I18n.get("quark.gui.config.nchildren", size));
	}

	@Override
	public void addWidgets(ZetaClient zc, Screen parent, ChangeSet changes, SectionDefinition def, Consumer<AbstractWidget> widgets) {
		widgets.accept(new PencilButton(zc, 230, 3, b -> Minecraft.getInstance().setScreen(new SectionScreen(zc, parent, changes, def))));
	}
}
