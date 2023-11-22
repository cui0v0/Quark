package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.registry.VariantRegistry;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;
import org.violetmoon.zeta.registry.IZetaItemColorProvider;

public class ZetaWallBlock extends WallBlock implements IZetaBlock, IZetaBlockColorProvider {

	private final IZetaBlock parent;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaWallBlock(IZetaBlock parent) {
		super(VariantRegistry.realStateCopy(parent));

		this.parent = parent;
		String resloc = parent.getModule().zeta.registryUtil.inheritQuark(parent, "%s_wall");
		parent.getModule().zeta.registry.registerBlock(this, resloc, true);
		parent.getModule().zeta.registry.setCreativeTab(this, "DECORATIONS");
		parent.getModule().zeta.renderLayerRegistry.mock(this, parent.getBlock());
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return parent.getModule();
	}

	@Override
	public ZetaWallBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public float[] getBeaconColorMultiplierZeta(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
		BlockState parentState = parent.getBlock().defaultBlockState();
		return parent.getModule().zeta.blockExtensions.get(parentState).getBeaconColorMultiplierZeta(parentState, world, pos, beaconPos);
	}

	@Override
	public @Nullable String getBlockColorProviderName() {
		return parent instanceof IZetaBlockColorProvider prov ? prov.getBlockColorProviderName() : null;
	}

	@Override
	public @Nullable String getItemColorProviderName() {
		return parent instanceof IZetaItemColorProvider prov ? prov.getItemColorProviderName() : null;
	}

}
