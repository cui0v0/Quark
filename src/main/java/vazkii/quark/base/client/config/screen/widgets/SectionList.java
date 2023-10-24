package vazkii.quark.base.client.config.screen.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import vazkii.quark.base.QuarkClient;
import vazkii.quark.base.client.config.screen.SectionScreen;
import vazkii.quark.base.client.handler.TopLayerTooltipHandler;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.Definition;
import vazkii.zeta.config.SectionDefinition;
import vazkii.zeta.config.ValueDefinition;
import vazkii.zeta.config.client.ClientDefinitionExt;

public class SectionList<T extends Definition> extends ScrollableWidgetList<SectionScreen, SectionList.Entry<T>> {

	public SectionList(SectionScreen parent) {
		super(parent);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void findEntries() {
		for(ValueDefinition<?> value : parent.section.getValues()) {
			addEntry((Entry<T>) new ValueDefinitionEntry<>(this.parent, this.parent.getChangeSet(), value));
		}

		Collection<SectionDefinition> subsections = parent.section.getSubsections();
		if(!subsections.isEmpty()) {
			addEntry((Entry<T>) new Divider()); //divider

			for(SectionDefinition section : parent.section.getSubsections())
				addEntry((Entry<T>) new SectionDefinitionEntry(this.parent, this.parent.getChangeSet(), section));
		}
	}

	public static abstract class Entry<T extends Definition> extends ScrollableWidgetList.Entry<Entry<T>> {
		protected final Minecraft mc = Minecraft.getInstance();
	}

	public static class Divider extends Entry<Definition> {
		@Override
		public void render(@NotNull PoseStack mstack, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
			String s = I18n.get("quark.gui.config.subcategories");
			mc.font.drawShadow(mstack, s, rowLeft + (float) (rowWidth / 2 - mc.font.width(s) / 2), rowTop + 7, 0x6666FF);
		}

		@Override
		public Component getNarration() {
			return Component.literal("");
		}
	}

	public static class DefinitionEntry<T extends Definition> extends Entry<T> {
		private final SectionScreen parent;
		private final ChangeSet changes;
		private final T def;
		private final ClientDefinitionExt<T> ext;

		public DefinitionEntry(SectionScreen parent, ChangeSet changes, T def) {
			this.parent = parent;
			this.changes = changes;
			this.def = def;

			this.ext = QuarkClient.ZETA_CLIENT.clientConfigManager.getExt(def);
			ext.addWidgets(parent, changes, def, children);
		}

		@Override
		public void render(@NotNull PoseStack mstack, int index, int rowTop, int rowLeft, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
			super.render(mstack, index, rowTop, rowLeft, rowWidth, rowHeight, mouseX, mouseY, hovered, partialTicks);

			int left = rowLeft + 10;
			int top = rowTop + 4;

			int effIndex = index + 1;
			if(def instanceof SectionDefinition)
				effIndex--; // compensate for the divider
			drawBackground(mstack, effIndex, rowTop, rowLeft, rowWidth, rowHeight, mouseX, mouseY, hovered);

			String name = def.getGuiDisplayName(I18n::get);
			if(parent.getChangeSet().isDirty(def))
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

			List<String> tooltip = new ArrayList<>(def.comment);
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
			if(ext != null)
				mc.font.drawShadow(mstack, ext.getSubtitle(changes, def), left, top + 10, 0x999999); //TODO: getSubtitle
		}

		@Override
		public Component getNarration() {
			return Component.literal(def.getGuiDisplayName(I18n::get));
		}
	}

	public static class ValueDefinitionEntry<X> extends DefinitionEntry<ValueDefinition<X>> {
		public ValueDefinitionEntry(SectionScreen parent, ChangeSet changes, ValueDefinition<X> def) {
			super(parent, changes, def);
		}
	}

	public static class SectionDefinitionEntry extends DefinitionEntry<SectionDefinition> {
		public SectionDefinitionEntry(SectionScreen parent, ChangeSet changes, SectionDefinition def) {
			super(parent, changes, def);
		}
	}
}
