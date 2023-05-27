package vazkii.quark.base.client.config.screen;

import net.minecraft.client.gui.components.AbstractWidget;

public class WidgetWrapper {

	public final AbstractWidget widget;
	public final int relativeX, relativeY;
	
	public WidgetWrapper(AbstractWidget widget) {
		this.widget = widget;
		this.relativeX = widget.getX();
		this.relativeY = widget.getY();
	}
	
	public void updatePosition(int currX, int currY) {
		widget.setX(currX + relativeX);
		widget.setY(currY + relativeY);
		widget.visible = true;
	}
	
}
