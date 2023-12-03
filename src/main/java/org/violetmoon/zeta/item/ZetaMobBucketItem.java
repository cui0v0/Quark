package org.violetmoon.zeta.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluid;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ZetaMobBucketItem extends MobBucketItem implements IZetaItem {

	private final ZetaModule module;

	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaMobBucketItem(Supplier<? extends EntityType<?>> entity, Supplier<? extends Fluid> fluid, Supplier<? extends SoundEvent> sound, String name, ZetaModule module) {
		super(entity, fluid, sound, (new Properties()).stacksTo(1));

		this.module = module;
		module.zeta.registry.registerItem(this, name);
		CreativeTabManager.addToCreativeTab(CreativeModeTabs.TOOLS_AND_UTILITIES, this);
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
