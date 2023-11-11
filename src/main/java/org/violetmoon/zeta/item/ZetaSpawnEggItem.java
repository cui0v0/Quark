package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaSpawnEggItem extends ForgeSpawnEggItem implements IZetaItem {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaSpawnEggItem(Supplier<EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, String regname, ZetaModule module, Properties properties) {
		super(type, primaryColor, secondaryColor, properties);

		module.zeta.registry.registerItem(this, regname);
		this.module = module;
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
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
