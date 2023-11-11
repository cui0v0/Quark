package org.violetmoon.zeta.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import org.violetmoon.zeta.block.ext.IZetaBlockExtensions;
import org.violetmoon.zeta.module.IDisableable;

/**
 * @author WireSegal
 * Created at 1:14 PM on 9/19/19.
 */
public interface IZetaBlock extends IZetaBlockExtensions, IDisableable<IZetaBlock> {

	default Block getBlock() {
		return (Block) this;
	}

	@Override
	default int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		if (state.getValues().containsKey(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
			return 0;

		Material material = state.getMaterial();
		if (material == Material.WOOL || material == Material.LEAVES)
			return 60;
		ResourceLocation loc = Registry.BLOCK.getKey(state.getBlock());
		if (loc != null && (loc.getPath().endsWith("_log") || loc.getPath().endsWith("_wood")) && state.getMaterial().isFlammable())
			return 5;
		return state.getMaterial().isFlammable() ? 20 : 0;
	}

	@Override
	default int getFireSpreadSpeedZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		if (state.getValues().containsKey(BlockStateProperties.WATERLOGGED) && state.getValue(BlockStateProperties.WATERLOGGED))
			return 0;

		Material material = state.getMaterial();
		if (material == Material.WOOL || material == Material.LEAVES)
			return 30;
		return state.getMaterial().isFlammable() ? 5 : 0;
	}

}
