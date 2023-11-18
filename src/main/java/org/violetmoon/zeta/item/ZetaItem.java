package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.zeta.item.ext.IZetaItemExtensions;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaItem extends Item implements IZetaItem, IZetaItemExtensions {
	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaItem(String regname, ZetaModule module, Properties properties) {
		super(properties);

		this.module = module;
		module.zeta.registry.registerItem(this, regname);

		if(module != null && module.category.isAddon())
			module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);
	}

	@Override
	public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
	}

	@Override
	public ZetaItem setCondition(BooleanSupplier enabledSupplier) {
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
