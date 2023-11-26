package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class VariantBookshelfBlock extends ZetaBlock {

	private final boolean flammable;
	
	public VariantBookshelfBlock(String type, ZetaModule module, boolean flammable) {
		super(type + "_bookshelf", module, Block.Properties.copy(Blocks.BOOKSHELF));
		this.flammable = flammable;
		setCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.BOOKSHELF, false);
	}
	
	@Override
	public boolean isFlammableZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return flammable;
	}
	
	@Override
	public float getEnchantPowerBonusZeta(BlockState state, LevelReader world, BlockPos pos) {
		return 1;
	}
}
