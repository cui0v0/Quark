package org.violetmoon.zeta.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;
import org.violetmoon.zeta.registry.IZetaItemColorProvider;
import org.violetmoon.zeta.registry.VariantRegistry;

import java.util.function.BooleanSupplier;

public class ZetaSlabBlock extends SlabBlock implements IZetaBlock, IZetaBlockColorProvider {

	public final IZetaBlock parent;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaSlabBlock(IZetaBlock parent) {
		super(VariantRegistry.realStateCopy(parent));

		this.parent = parent;
		String resloc = parent.getModule().zeta.registryUtil.inheritQuark(parent, "%s_slab");
		parent.getModule().zeta.registry.registerBlock(this, resloc, true);
		parent.getModule().zeta.registry.setCreativeTab(this, "BUILDING_BLOCKS");
		parent.getModule().zeta.renderLayerRegistry.mock(this, parent.getBlock());
	}

	@Override
	public boolean isFlammableZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		BlockState parentState = parent.getBlock().defaultBlockState();
		return parent.getModule().zeta.blockExtensions.get(parentState).isFlammableZeta(parentState, world, pos, face);
	}

	@Override
	public int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		BlockState parentState = parent.getBlock().defaultBlockState();
		return parent.getModule().zeta.blockExtensions.get(parentState).getFlammabilityZeta(parentState, world, pos, face);
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return parent.getModule();
	}

	@Override
	public ZetaSlabBlock setCondition(BooleanSupplier enabledSupplier) {
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
