package org.violetmoon.quark.content.building.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.violetmoon.quark.content.building.module.ThatchModule;
import org.violetmoon.zeta.block.ZetaFlammableBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class ThatchBlock extends ZetaFlammableBlock {

	public ThatchBlock(ZetaModule module) {
		super("thatch", module, CreativeModeTab.TAB_BUILDING_BLOCKS, 300,
				Block.Properties.of(Material.GRASS, MapColor.COLOR_YELLOW)
				.strength(0.5F)
				.sound(SoundType.GRASS));
	}

	@Override
	public void fallOn(@NotNull Level worldIn, @NotNull BlockState state, @NotNull BlockPos pos, Entity entityIn, float fallDistance) {
		entityIn.causeFallDamage(fallDistance, (float) ThatchModule.fallDamageMultiplier, DamageSource.FALL);
	}

}
