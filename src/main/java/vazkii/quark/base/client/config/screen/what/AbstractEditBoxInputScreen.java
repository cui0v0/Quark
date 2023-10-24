package vazkii.quark.base.client.config.screen.what;

import javax.annotation.Nonnull;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import vazkii.zeta.config.ChangeSet;
import vazkii.zeta.config.ValueDefinition;

public abstract class AbstractEditBoxInputScreen<T> extends AbstractInputScreen2<T> {
	private EditBox input;

	public AbstractEditBoxInputScreen(Screen parent, ChangeSet changes, ValueDefinition<T> valueDef) {
		super(parent, changes, valueDef);
	}

	@Override
	public void render(@Nonnull PoseStack mstack, int mouseX, int mouseY, float partialTicks) {
		super.render(mstack, mouseX, mouseY, partialTicks);

		drawCenteredString(mstack, font, Component.literal(ext.getGuiDisplayName(changes, def)).withStyle(ChatFormatting.BOLD), width / 2, 20, 0xFFFFFF);
		drawCenteredString(mstack, font, I18n.get("quark.gui.config.defaultvalue", def.defaultValue), width / 2, 30, 0xFFFFFF);

		input.render(mstack, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void init() {
		super.init();

		input = new EditBox(font, width / 2 - 100, 60, 200, 20, Component.literal(""));
		//input.setFilter(s -> fromString(s) != null); //ALlow temporarily editing incorrect values
		input.setMaxLength(maxStringLength());
		input.setResponder(this::onEdit);

		setTo(changes.get(def));

		setInitialFocus(input);
		addWidget(input);
	}

	protected void onEdit(String newString) {
		T parsed = fromString(newString);
		if(parsed != null && def.validate(parsed) && newString.length() < maxStringLength()) {
			changes.set(def, parsed);
			input.setTextColor(0xE0_E0_E0);
			updateButtonStatus(true);
		} else {
			input.setTextColor(0xDD_33_22);
			updateButtonStatus(false);
		}
	}

	@Override
	protected void setTo(T value) {
		//Test that the object isnt in some state where it'll be rejected
		String asString = toString(value);
		T roundtrip = fromString(asString);
		if(roundtrip == null)
			input.setValue(toString(def.defaultValue));
		else
			input.setValue(asString);
	}

	protected String toString(T thing) {
		return thing.toString();
	}

	protected int maxStringLength() {
		return 256;
	}

	protected abstract @Nullable T fromString(String string);
}
