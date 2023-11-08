package vazkii.zeta.event.bus.helpers;

import net.minecraft.world.entity.player.Player;

//fixme Move this into a lifelong home & make other interfaces use this more
public interface PlayerGetter { // TODO rename to getPlayer to prevent overload with LivingGetter
    Player getEntity();
}
