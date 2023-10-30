package vazkii.zeta.event;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import vazkii.zeta.event.bus.IZetaLoadEvent;

public interface ZEntityAttributeCreation extends IZetaLoadEvent {
	void put(EntityType<? extends LivingEntity> entity, AttributeSupplier map);
}
