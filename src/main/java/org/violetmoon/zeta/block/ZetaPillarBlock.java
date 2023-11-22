package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.RotatedPillarBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaPillarBlock extends RotatedPillarBlock implements IZetaBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaPillarBlock(String regname, ZetaModule module, String creativeTab, Properties properties) {
		super(properties);
		this.module = module;
		module.zeta.registry.registerBlock(this, regname, true);
		module.zeta.registry.setCreativeTab(this, creativeTab);
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public ZetaPillarBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}
}
