package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaMobBucketItem extends MobBucketItem implements IZetaItem {

	private final ZetaModule module;

	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaMobBucketItem(Supplier<? extends EntityType<?>> entity, Supplier<? extends Fluid> fluid, Supplier<? extends SoundEvent> sound, String name, ZetaModule module) {
		super(entity, fluid, sound, (new Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC));

		this.module = module;
		module.zeta.registry.registerItem(this, name);
	}

	@Override
	public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
	}

	@Override
	public ZetaMobBucketItem setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

}
