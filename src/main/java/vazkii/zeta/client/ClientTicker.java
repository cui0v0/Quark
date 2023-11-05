package vazkii.zeta.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import vazkii.zeta.client.event.ZEndClientTick;
import vazkii.zeta.client.event.ZRenderTick;
import vazkii.zeta.event.bus.PlayEvent;

public final class ClientTicker {
	public int ticksInGame = 0;
	public float partialTicks = 0;
	public float delta = 0;
	public float total = 0;

	@PlayEvent
	public void onRenderTick(ZRenderTick event) {
		if(event.isStartPhase())
			partialTicks = event.getRenderTickTime();
		else
			endRenderTick();
	}

	@PlayEvent
	public void onEndClientTick(ZEndClientTick event) {
		Screen gui = Minecraft.getInstance().screen;
		if(gui == null || !gui.isPauseScreen()) {
			ticksInGame++;
			partialTicks = 0;
		}

		endRenderTick();
	}

	public void endRenderTick() {
		float oldTotal = total;
		total = ticksInGame + partialTicks;
		delta = total - oldTotal;
	}
}
