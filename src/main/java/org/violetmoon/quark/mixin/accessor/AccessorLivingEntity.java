package org.violetmoon.quark.mixin.accessor;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface AccessorLivingEntity {
//fixme
//	@Invoker("createLootContext")
//	LootContext.Builder quark$createLootContext(boolean playerLoot, DamageSource source);

	@Accessor("lastHurtByPlayerTime")
	int quark$lastHurtByPlayerTime();

}
