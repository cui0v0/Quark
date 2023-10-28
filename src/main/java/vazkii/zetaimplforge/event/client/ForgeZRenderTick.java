package vazkii.zetaimplforge.event.client;

import net.minecraftforge.event.TickEvent;
import vazkii.zeta.client.event.ZRenderTick;
import vazkii.zeta.event.bus.FiredAs;

@FiredAs(ZRenderTick.class)
public record ForgeZRenderTick(TickEvent.RenderTickEvent e) implements ZRenderTick {
	@Override
	public boolean isEndPhase() {
		return e.phase == TickEvent.Phase.END;
	}
}
