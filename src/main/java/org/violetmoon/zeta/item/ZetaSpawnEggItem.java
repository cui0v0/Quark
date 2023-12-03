package org.violetmoon.zeta.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class ZetaSpawnEggItem extends ForgeSpawnEggItem implements IZetaItem {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaSpawnEggItem(Supplier<EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, String regname, ZetaModule module, Properties properties) {
		super(type, primaryColor, secondaryColor, properties);

		module.zeta.registry.registerItem(this, regname);
		CreativeTabManager.addToCreativeTab(CreativeModeTabs.SPAWN_EGGS, this);
		this.module = module;
	}

	@Override
	public ZetaSpawnEggItem setCondition(BooleanSupplier enabledSupplier) {
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
