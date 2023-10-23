package vazkii.quark.base.client.config.screen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.text.WordUtils;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.client.renderer.PanoramaRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import vazkii.quark.api.config.IConfigCategory;
import vazkii.quark.base.Quark;
import vazkii.quark.base.client.config.IngameConfigHandler;
import vazkii.quark.base.client.config.screen.widgets.CheckboxButton;
import vazkii.quark.base.client.config.screen.widgets.IconButton;
import vazkii.quark.base.client.config.screen.widgets.SocialButton;
import vazkii.quark.base.handler.ContributorRewardHandler;
import vazkii.quark.base.handler.GeneralConfig;
import vazkii.zeta.module.ZetaCategory;

public class QuarkConfigHomeScreen extends AbstractQScreen {

	private static final CubeMap CUBE_MAP = new CubeMap(new ResourceLocation(Quark.MOD_ID, "textures/misc/panorama/panorama"));
	private static final PanoramaRenderer PANORAMA = new PanoramaRenderer(CUBE_MAP);
	private float time;

	public QuarkConfigHomeScreen(Screen parent) {
		super(parent);
	}

	@Override
	protected void init() {
		super.init();

		List<ZetaCategory> categories = Quark.ZETA.modules.getInhabitedCategories();
		int buttonCount = categories.size() + 1;

		final int perLine = 3;
		List<Integer> categoryButtonXPositions = new ArrayList<>(buttonCount);
		for(int i = 0; i < buttonCount; i += perLine)
			categoryButtonXPositions.addAll(centeredRow(width / 2, 120, 10, Math.min(buttonCount - i, perLine)));

		for(int i = 0; i < buttonCount; i++) {
			int row = i / perLine;

			int x = categoryButtonXPositions.get(i);
			int y = 70 + row * 23;
			int bWidth = 120;

			if(i < categories.size()) {
				//a category button
				ZetaCategory category = categories.get(i);
				IConfigCategory ingameCategory = IngameConfigHandler.INSTANCE.getConfigCategory(category);

				bWidth -= 20; //room for the checkbox
				Button mainButton = addRenderableWidget(new IconButton(x, y, bWidth, 20, componentFor(ingameCategory), category.icon.get(), categoryLink(ingameCategory)));
				Button checkButton = addRenderableWidget(new CheckboxButton(x + bWidth, y, IngameConfigHandler.INSTANCE.getCategoryEnabledObject(category)));

				boolean active = category.modsLoaded(Quark.ZETA);
				mainButton.active = active;
				checkButton.active = active;
			} else {
				//"General Settings"
				IConfigCategory generalSettings = IngameConfigHandler.INSTANCE.getConfigCategory(null);
				addRenderableWidget(new Button(x, y, bWidth, 20, componentFor(generalSettings), categoryLink(generalSettings)));
			}
		}

		//save
		addRenderableWidget(new Button(width / 2 - 100, height - 30, 200, 20, Component.translatable("quark.gui.config.save"), this::commit));

		//social buttons
		List<Integer> socialButtonPlacements = centeredRow(width / 2, 20, 5, 5);
		Iterator<Integer> iter = socialButtonPlacements.iterator();
		addRenderableWidget(new SocialButton(iter.next(), height - 55, Component.translatable("quark.gui.config.social.website"), 0x48ddbc, 0, webLink("https://quarkmod.net")));
		addRenderableWidget(new SocialButton(iter.next(), height - 55, Component.translatable("quark.gui.config.social.discord"), 0x7289da, 1, webLink("https://discord.gg/vm")));
		addRenderableWidget(new SocialButton(iter.next(), height - 55, Component.translatable("quark.gui.config.social.patreon"), 0xf96854, 2, webLink("https://patreon.com/vazkii")));
		addRenderableWidget(new SocialButton(iter.next(), height - 55, Component.translatable("quark.gui.config.social.forum"), 0xb650d8, 3, webLink("https://forum.violetmoon.org")));
		addRenderableWidget(new SocialButton(iter.next(), height - 55, Component.translatable("quark.gui.config.social.twitter"), 0x1da1f2, 4, webLink("https://twitter.com/VazkiiMods")));
	}

	private static List<Integer> centeredRow(int centerX, int buttonWidth, int hpad, int count) {
		// https://i.imgur.com/ozRA3xw.png

		int slop = (count % 2 == 0 ? hpad : buttonWidth) / 2;
		int fullButtonsLeftOfCenter = count / 2; //rounds down
		int fullPaddingsLeftOfCenter = Math.max(0, (count - 1) / 2); //rounds down
		int startX = centerX - slop - (fullButtonsLeftOfCenter * buttonWidth) - (fullPaddingsLeftOfCenter * hpad);

		List<Integer> result = new ArrayList<>(count);
		int x = startX;
		for(int i = 0; i < count; i++) {
			result.add(x);
			x += buttonWidth + hpad;
		}
		return result;
	}

	private static Component componentFor(IConfigCategory c) {
		MutableComponent comp = Component.translatable("quark.category." + c.getName());

		if(c.isDirty())
			comp.append(Component.literal("*").withStyle(ChatFormatting.GOLD));

		return comp;
	}

	public void commit(Button button) {
		IngameConfigHandler.INSTANCE.commit();
		returnToParent(button);
	}

	@Override
	public void render(@Nonnull PoseStack mstack, int mouseX, int mouseY, float partialTicks) {
		time += partialTicks;

		Minecraft mc = Minecraft.getInstance();
		if(mc.level == null) {
			float spin = partialTicks * 2;
			float blur = 0.85F;

			if(time < 20F && !GeneralConfig.disableQMenuEffects) {
				spin += (20F - time);
				blur = (time / 20F) * 0.75F + 0.1F;
			}

			PANORAMA.render(spin, blur);
		} else renderBackground(mstack);

		int boxWidth = 400;
		fill(mstack, width / 2 - boxWidth / 2, 0, width / 2 + boxWidth / 2, this.height, 0x66000000);
		fill(mstack, width / 2 - boxWidth / 2 - 1, 0, width / 2 - boxWidth / 2, this.height, 0x66999999); // nice
		fill(mstack, width / 2 + boxWidth / 2, 0, width / 2 + boxWidth / 2 + 1, this.height, 0x66999999);

		super.render(mstack, mouseX, mouseY, partialTicks);

		drawCenteredString(mstack, font, ChatFormatting.BOLD + I18n.get("quark.gui.config.header", WordUtils.capitalizeFully(Quark.MOD_ID)), width / 2, 15, 0x48ddbc);
		drawCenteredString(mstack, font, I18n.get("quark.gui.config.subheader1", ChatFormatting.LIGHT_PURPLE, ContributorRewardHandler.featuredPatron, ChatFormatting.RESET), width / 2, 28, 0x9EFFFE);
		drawCenteredString(mstack, font, I18n.get("quark.gui.config.subheader2"), width / 2, 38, 0x9EFFFE);
	}

}
