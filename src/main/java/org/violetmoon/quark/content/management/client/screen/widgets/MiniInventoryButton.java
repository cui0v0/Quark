package org.violetmoon.quark.content.management.client.screen.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.handler.MiscUtil;
import org.violetmoon.zeta.util.BooleanSuppliers;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MiniInventoryButton extends Button {

	private final Consumer<List<String>> tooltip;
	private final int type;
	private final AbstractContainerScreen<?> parent;
	private final int startX;

	private BooleanSupplier shiftTexture = BooleanSuppliers.FALSE;

	public MiniInventoryButton(AbstractContainerScreen<?> parent, int type, int x, int y, Consumer<List<String>> tooltip, OnPress onPress) {
		super(new Button.Builder(Component.literal(""), onPress).size(10, 10).pos(parent.getGuiLeft() + x, parent.getGuiTop() + y));
		this.parent = parent;
		this.type = type;
		this.tooltip = tooltip;
		this.startX = x;
	}

	public MiniInventoryButton(AbstractContainerScreen<?> parent, int type, int x, int y, String tooltip, OnPress onPress) {
		this(parent, type, x, y, (t) -> t.add(I18n.get(tooltip)), onPress);
	}

	public MiniInventoryButton setTextureShift(BooleanSupplier func) {
		shiftTexture = func;
		return this;
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		if(parent instanceof RecipeUpdateListener)
			setX(parent.getGuiLeft() + startX);

		super.render(guiGraphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		int u = type * width;
		int v = 25 + (isHovered ? height : 0);
		if(shiftTexture.getAsBoolean())
			v += (height * 2);

		guiGraphics.blit(MiscUtil.GENERAL_ICONS, getX(), getY(), u, v, width, height);

		//TODO: change API to take Components so this awkward stream/map dance isn't needed
		//Even better TODO: change API to use vanilla setTooltip
		if(isHovered)
			guiGraphics.renderComponentTooltip(Minecraft.getInstance().font, local$getToolTip().stream().map(Component::literal).collect(Collectors.toList()), mouseX, mouseY);
	}

	@NotNull
	@Override
	protected MutableComponent createNarrationMessage() {
		List<String> tooltip = local$getToolTip();
		return tooltip.isEmpty() ? Component.literal("") : Component.translatable("gui.narrate.button", local$getToolTip().get(0));
	}

	/*
	 * Prefixed with local$ to prevent clashing
	 */
	public List<String> local$getToolTip() {
		List<String> list = new LinkedList<>();
		tooltip.accept(list);
		return list;
	}

}
