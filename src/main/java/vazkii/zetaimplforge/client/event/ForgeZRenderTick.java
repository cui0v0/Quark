package vazkii.zetaimplforge.client.event;

import net.minecraftforge.event.TickEvent;
import vazkii.zeta.client.event.ZRenderTick;

public record ForgeZRenderTick(TickEvent.RenderTickEvent e) implements ZRenderTick {
	@Override
	public boolean isEndPhase() {
		return e.phase == TickEvent.Phase.END;
	}
}
