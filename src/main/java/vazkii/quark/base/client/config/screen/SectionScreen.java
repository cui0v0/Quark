package vazkii.quark.base.client.config.screen;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.quark.base.Quark;
import vazkii.quark.base.client.config.screen.widgets.ScrollableWidgetList;
import vazkii.quark.base.client.config.screen.widgets.SectionList;
import vazkii.zeta.config.SectionDefinition;

public class SectionScreen extends AbstractScrollingWidgetScreen {

	public final SectionDefinition section;
	private String breadcrumbs;

	public SectionScreen(Screen parent, SectionDefinition section) {
		super(parent);
		this.section = section;
	}

	@Override
	protected void init() {
		super.init();

//		breadcrumbs = section.getName();
//		IConfigCategory currCategory = section.getParent();
//		while(currCategory != null) {
//			breadcrumbs = String.format("%s > %s", currCategory.getName(), breadcrumbs);
//			currCategory = currCategory.getParent();
//		}
//		breadcrumbs = String.format("> %s", breadcrumbs);
		breadcrumbs = section.name; //TODO readd breadcrumbs
	}

	@Override
	public void render(@Nonnull PoseStack mstack, int mouseX, int mouseY, float partialTicks) {
		super.render(mstack, mouseX, mouseY, partialTicks);

		int left = 20;

		String modName = WordUtils.capitalizeFully(Quark.MOD_ID);
		font.draw(mstack, ChatFormatting.BOLD + I18n.get("quark.gui.config.header", modName), left, 10, 0x48ddbc);
		font.draw(mstack, breadcrumbs, left, 20, 0xFFFFFF);
	}

	@Override
	protected ScrollableWidgetList<?, ?> createWidgetList() {
		return new SectionList<>(this);
	}

	@Override
	protected void onClickDefault(Button b) {
		getChangeSet().resetToDefault(section);
	}

	@Override
	protected void onClickDiscard(Button b) {
		getChangeSet().removeChange(section);
	}

	@Override
	protected boolean isDirty() {
		return getChangeSet().isDirty(section);
	}

}
