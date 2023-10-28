package vazkii.zetaimplforge.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import vazkii.zeta.event.ZLivingTick;

public record ForgeZLivingTick(LivingEvent.LivingTickEvent e) implements ZLivingTick {
	@Override
	public LivingEntity getEntity() {
		return e.getEntity();
	}
}
