package org.violetmoon.zeta.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.violetmoon.zeta.client.event.play.ZRenderTick;
import org.violetmoon.zeta.event.bus.PlayEvent;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TopLayerTooltipHandler {

	private List<Component> tooltip;
	private int tooltipX, tooltipY;

	@PlayEvent
	public void renderTick(ZRenderTick event) {
		if(tooltip != null && event.isEndPhase()) {
			Minecraft mc = Minecraft.getInstance();
			Screen screen = mc.screen;

			GuiGraphics guiGraphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());

			if(screen != null)
				guiGraphics.renderTooltip(mc.font, tooltip, Optional.empty(), tooltipX, tooltipY);

			tooltip = null;
		}
	}

	public void setTooltip(List<String> tooltip, int x, int y) {
		this.tooltip = tooltip.stream().map(Component::literal).collect(Collectors.toList());
		this.tooltipX = x;
		this.tooltipY = y;
	}

}
