package vazkii.zetaimplforge.event.client;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.zeta.event.bus.FiredAs;
import vazkii.zeta.client.event.ZClientSetup;

@FiredAs(ZClientSetup.class)
public record ForgeZClientSetup(FMLClientSetupEvent e) implements ZClientSetup {
	@Override
	public void enqueueWork(Runnable run) {
		e.enqueueWork(run);
	}
}
