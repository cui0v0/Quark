package vazkii.zeta.client.event;

import net.minecraft.client.player.Input;
import net.minecraft.world.entity.player.Player;
import vazkii.zeta.event.bus.IZetaPlayEvent;

//NB. forge fires this in LocalPlayer.aiStep
public interface ZInputUpdate extends IZetaPlayEvent {
	Input getInput();
	Player getEntity();
}
