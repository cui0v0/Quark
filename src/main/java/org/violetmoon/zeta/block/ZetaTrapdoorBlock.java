package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry;

public class ZetaTrapdoorBlock extends TrapDoorBlock implements IZetaBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaTrapdoorBlock(String regname, ZetaModule module, String creativeTab, Properties properties) {
		super(properties);
		this.module = module;

		module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
		module.zeta.registry.registerBlock(this, regname, true);
		module.zeta.registry.setCreativeTab(this, creativeTab);

	}

	@Override
	public boolean isLadderZeta(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
		if(state.getValue(OPEN)) {
			BlockPos downPos = pos.below();
			BlockState down = level.getBlockState(downPos);
			return module.zeta.blockExtensions.get(down).makesOpenTrapdoorAboveClimbableZeta(down, level, downPos, state);
		} else return false;
	}

	@Override
	public ZetaTrapdoorBlock setCondition(BooleanSupplier enabledSupplier) {
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
