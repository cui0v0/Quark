package vazkii.zetaimplforge.client.event;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vazkii.zeta.client.event.ZClientSetup;

public record ForgeZClientSetup(FMLClientSetupEvent e) implements ZClientSetup {
	@Override
	public void enqueueWork(Runnable run) {
		e.enqueueWork(run);
	}
}
