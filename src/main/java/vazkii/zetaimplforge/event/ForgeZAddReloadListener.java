package vazkii.zetaimplforge.event;

import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import vazkii.zeta.event.ZAddReloadListener;

public class ForgeZAddReloadListener implements ZAddReloadListener {
	private final AddReloadListenerEvent e;

	public ForgeZAddReloadListener(AddReloadListenerEvent e) {
		this.e = e;
	}

	@Override
	public void addListener(PreparableReloadListener listener) {e.addListener(listener);}

	@Override
	public ReloadableServerResources getServerResources() {return e.getServerResources();}
}
