package vazkii.quark.base.client.config.screen.what;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import vazkii.quark.base.client.config.screen.WidgetWrapper;
import vazkii.quark.base.client.config.screen.widgets.ScrollableWidgetList;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public class ListInputScreen2 extends AbstractInputScreen2<List<String>> {
	protected final ScrollableWidgetList<ListInputScreen2, Entry> wow;

	public ListInputScreen2(Screen parent, ChangeSet changes, ValueDefinition<List<String>> def) {
		super(parent, changes, def);

		//its jankye
		wow = new ScrollableWidgetList<>(this) {
			@Override
			protected void findEntries() {
				for(int i = 0; i < list().size(); i++) {
					addEntry(new ListInputScreen2.Entry(i));
				}
			}
		};
	}

	@Override
	protected void setTo(List<String> value) {

	}

	@Override
	public void render(@NotNull PoseStack mstack, int mouseX, int mouseY, float partialTicks) {
		super.render(mstack, mouseX, mouseY, partialTicks);
		wow.render(mstack, mouseX, mouseY, partialTicks);
	}

	protected List<String> list() {
		return changes.get(def);
	}

	protected String getString(int index) {
		List<String> list = list();
		if(index < list.size())
			return list.get(index);
		else
			return null;
	}

	protected void setString(int index, String s) {
		List<String> list = list();
		while(list.size() < index + 1) //uhh
			list.add("");
		list.set(index, s);
	}

	protected void add() {
		list().add("");
	}

	protected void remove(int idx) {
		list().remove(idx);
	}

	class Entry extends ScrollableWidgetList.Entry<Entry> {
		private final int index;

		public Entry(int index) {
			this.index = index;

			String here = getString(index);
			if(getString(index) != null) {
				Minecraft mc = Minecraft.getInstance();
				EditBox field = new EditBox(mc.font, 10, 3, 210, 20, Component.literal(""));
				field.setMaxLength(256);
				field.setValue(here);
				field.moveCursorTo(0);
				field.setResponder(str -> setString(index, str));
				children.add(new WidgetWrapper(field));

				children.add(new WidgetWrapper(new Button(230, 3, 20, 20, Component.literal("-").withStyle(ChatFormatting.RED), b -> remove(index))));
			} else {
				children.add(new WidgetWrapper(new Button(10, 3, 20, 20, Component.literal("+").withStyle(ChatFormatting.GREEN), b -> add())));
			}
		}

		@Override
		public Component getNarration() {
			return Component.literal(Optional.ofNullable(getString(index)).orElse(""));
		}
	}
}
