package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;

import org.violetmoon.zeta.module.ZetaModule;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class ZetaSignItem extends SignItem implements IZetaItem {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaSignItem(ZetaModule module, Block sign, Block wallSign) {
		super(new Item.Properties().stacksTo(16), sign, wallSign);

		String resloc = module.zeta.registryUtil.inherit(sign, "%s");
		module.zeta.registry.registerItem(this, resloc);
		setCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.CHEST, true);
		this.module = module;
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
