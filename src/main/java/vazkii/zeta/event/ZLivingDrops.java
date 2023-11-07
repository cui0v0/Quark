package vazkii.zeta.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import vazkii.zeta.event.bus.IZetaPlayEvent;
import vazkii.zeta.event.bus.helpers.LivingGetter;

import java.util.Collection;

public interface ZLivingDrops extends IZetaPlayEvent, LivingGetter {
	Collection<ItemEntity> getDrops();
	DamageSource getSource();
}
