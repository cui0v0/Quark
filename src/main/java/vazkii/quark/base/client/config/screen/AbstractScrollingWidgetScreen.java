package vazkii.quark.base.client.config.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import vazkii.quark.base.client.config.screen.widgets.ScrollableWidgetList;

import javax.annotation.Nonnull;

import java.util.LinkedList;
import java.util.List;

//widgets are a vanilla concept, lists are a vanilla concept, but the two don't mesh very well
@Deprecated
public abstract class AbstractScrollingWidgetScreen extends AbstractQScreen {

	private final List<AbstractWidget> scrollingWidgets = new LinkedList<>();
	private ScrollableWidgetList<?, ?> elementList;

	private Button resetButton;

	public AbstractScrollingWidgetScreen(Screen parent) {
		super(parent);
	}

	@Override
	protected void init() {
		super.init();

		int pad = 3;
		int bWidth = 121;
		int left = (width - (bWidth + pad) * 3) / 2;
		int vStart = height - 30;

		addRenderableWidget(new Button(left, vStart, bWidth, 20, Component.translatable("quark.gui.config.default"), this::onClickDefault));
		addRenderableWidget(resetButton = new Button(left + bWidth + pad, vStart, bWidth, 20, Component.translatable("quark.gui.config.discard"), this::onClickDiscard));
		addRenderableWidget(new Button(left + (bWidth + pad) * 2, vStart, bWidth, 20, Component.translatable("gui.done"), this::onClickDone));

		elementList = createWidgetList();
		addWidget(elementList);
		refresh();
	}

	@Override
	public void tick() {
		super.tick();

		resetButton.active = isDirty();
	}

	public void refresh() {
		for (AbstractWidget widget : scrollingWidgets)
			removeWidget(widget);
		scrollingWidgets.clear();

		elementList.populate(w -> {
			scrollingWidgets.add(w);
			if(w instanceof Button)
				addRenderableWidget(w);
			else addWidget(w);
		});
	}

	@Override
	public void render(@Nonnull PoseStack mstack, int mouseX, int mouseY, float partialTicks) {
		renderBackground(mstack);

		//render the element list. each element in the list draws its own widgets
		elementList.render(mstack, mouseX, mouseY, partialTicks);

		//make all scrolling widgets invisible so super.render won't draw them again
		scrollingWidgets.forEach(w -> w.visible = false);

		//call super.render, which renders all visible widgets
		//it *would* render the scrolling widgets too, but we just disabled their visibility
		super.render(mstack, mouseX, mouseY, partialTicks);

		//make all scrolling widgets visible so they can be clicked on
		scrollingWidgets.forEach(w -> w.visible = true);
	}

	protected abstract ScrollableWidgetList<?, ?> createWidgetList();
	protected abstract void onClickDefault(Button b);
	protected abstract void onClickDiscard(Button b);
	protected abstract boolean isDirty();

	protected void onClickDone(Button b) {
		returnToParent(b);
	}

}
