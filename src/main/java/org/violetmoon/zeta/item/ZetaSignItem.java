package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaSignItem extends SignItem implements IZetaItem {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaSignItem(ZetaModule module, Block sign, Block wallSign) {
		super(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_DECORATIONS), sign, wallSign);

		String resloc = module.zeta.registryUtil.inherit(sign, "%s");
		module.zeta.registry.registerItem(this, resloc);
		this.module = module;
	}

	@Override
	public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
	}

	@Override
	public ZetaSignItem setCondition(BooleanSupplier enabledSupplier) {
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
