package vazkii.quark.base.client.config.screen;

import org.apache.commons.lang3.text.WordUtils;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import vazkii.quark.api.config.IConfigCategory;
import vazkii.quark.base.Quark;
import vazkii.quark.base.client.config.ConfigObject;
import vazkii.quark.base.client.config.screen.widgets.ConfigElementList;
import vazkii.quark.base.client.config.screen.widgets.ScrollableWidgetList;

import javax.annotation.Nonnull;

@Deprecated
public class CategoryScreen extends AbstractScrollingWidgetScreen {

	public final IConfigCategory category;
	private String breadcrumbs;

	public CategoryScreen(Screen parent, IConfigCategory category) {
		super(parent);
		this.category = category;
	}

	@Override
	protected void init() {
		super.init();

		breadcrumbs = category.getName();
		IConfigCategory currCategory = category.getParent();
		while(currCategory != null) {
			breadcrumbs = String.format("%s > %s", currCategory.getName(), breadcrumbs);
			currCategory = currCategory.getParent();
		}
		breadcrumbs = String.format("> %s", breadcrumbs);
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
		return new ConfigElementList<ConfigObject<?>>(this);
	}

	@Override
	protected void onClickDefault(Button b) {
		category.reset(true);
	}

	@Override
	protected void onClickDiscard(Button b) {
		category.reset(false);
	}

	@Override
	protected boolean isDirty() {
		return category.isDirty();
	}

}
