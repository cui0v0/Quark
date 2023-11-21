package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaStandingSignBlock extends StandingSignBlock implements IZetaBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaStandingSignBlock(String regname, ZetaModule module, WoodType type, Properties properties) {
		super(properties, type);
		this.module = module;

		module.zeta.registry.registerBlock(this, regname, false);
	}

	@Override
	public ZetaStandingSignBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return module;
	}

}
