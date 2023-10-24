package vazkii.quark.base.client.config.screen.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import vazkii.quark.base.client.config.screen.SectionScreen;
import vazkii.quark.base.client.handler.TopLayerTooltipHandler;
import vazkii.zeta.config.Definition;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.ValueDefinition;

public class SectionList<T extends Definition> extends ScrollableWidgetList<SectionScreen, SectionList.Entry<T>> {

	public SectionList(SectionScreen parent) {
		super(parent);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void findEntries() {
		for(ValueDefinition<?> value : parent.section.getValues()) {
			addEntry(new Entry<>(this.parent, (T) value));
		}

		Collection<SectionDefinition> subsections = parent.section.getSubsections();
		if(!subsections.isEmpty()) {
			addEntry(new Entry<>(this.parent, null)); //divider

			for(SectionDefinition section : parent.section.getSubsections())
				addEntry(new Entry<>(this.parent, (T) section));
		}
	}

	public static final class Entry<T extends Definition> extends ScrollableWidgetList.Entry<Entry<T>> {

		private final SectionScreen parent;
		private final T element;

		public Entry(SectionScreen parent, T element) {
			this.parent = parent;
			this.element = element;

			//TODO: IWidgetProvider but side safe
			//if(element != null)
			//	element.addWidgets(parent, element, children);
		}

		@Override
		public void render(@Nonnull PoseStack mstack, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
			super.render(mstack, index, rowTop, rowLeft, rowWidth, rowHeight, mouseX, mouseY, hovered, partialTicks);

			Minecraft mc = Minecraft.getInstance();

			if(element != null) {
				int left = rowLeft + 10;
				int top = rowTop + 4;

				int effIndex = index + 1;
				if(element instanceof SectionDefinition)
					effIndex--; // compensate for the divider
				drawBackground(mstack, effIndex, rowTop, rowLeft, rowWidth, rowHeight, mouseX, mouseY, hovered);

				String name = element.getGuiDisplayName(Collections.emptyList(), I18n::get);
				if(parent.getChangeSet().isDirty(element))
					name += ChatFormatting.GOLD + "*";

				int len = mc.font.width(name);
				int maxLen = rowWidth - 85;
				String originalName = null;
				if(len > maxLen) {
					originalName = name;
					do {
						name = name.substring(0, name.length() - 1);
						len = mc.font.width(name);
					} while(len > maxLen);

					name += "...";
				}

				List<String> tooltip = new ArrayList<>(element.comment);
				if(originalName != null) {
					if(tooltip.isEmpty()) {
						tooltip = new LinkedList<>();
						tooltip.add(originalName);
					} else {
						tooltip.add(0, "");
						tooltip.add(0, originalName);
					}
				}

				if(!tooltip.isEmpty()) {
					int hoverLeft = left + mc.font.width(name + " ");
					int hoverRight = hoverLeft + mc.font.width("(?)");

					name += (ChatFormatting.AQUA + " (?)");
					if(mouseX >= hoverLeft && mouseX < hoverRight && mouseY >= top && mouseY < (top + 10))
						TopLayerTooltipHandler.setTooltip(tooltip, mouseX, mouseY);
				}

				mc.font.drawShadow(mstack, name, left, top, 0xFFFFFF);
				//mc.font.drawShadow(mstack, element.getSubtitle(), left, top + 10, 0x999999); //TODO: getSubtitle
			} else {
				String s = I18n.get("quark.gui.config.subcategories");
				mc.font.drawShadow(mstack, s, rowLeft + (float) (rowWidth / 2 - mc.font.width(s) / 2), rowTop + 7, 0x6666FF);
			}
		}

		@Nonnull
		@Override
		public Component getNarration() {
			return Component.literal(element == null ? "" : element.getGuiDisplayName(Collections.emptyList(), I18n::get));
		}

	}

}
