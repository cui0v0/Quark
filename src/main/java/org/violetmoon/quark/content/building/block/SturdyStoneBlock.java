package org.violetmoon.quark.content.building.block;

import javax.annotation.Nonnull;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class SturdyStoneBlock extends ZetaBlock {

	public SturdyStoneBlock(ZetaModule module) {
		super("sturdy_stone", module, CreativeModeTab.TAB_BUILDING_BLOCKS,
				Block.Properties.of(Material.STONE)
				.requiresCorrectToolForDrops()
				.strength(4F, 10F)
				.sound(SoundType.STONE));
	}

	@Nonnull
	@Override
	public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
		return PushReaction.BLOCK;
	}

}
