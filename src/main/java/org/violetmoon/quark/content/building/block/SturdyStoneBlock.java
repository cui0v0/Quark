package org.violetmoon.quark.content.building.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.violetmoon.zeta.block.OldMaterials;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class SturdyStoneBlock extends ZetaBlock {

	public SturdyStoneBlock(ZetaModule module) {
		super("sturdy_stone", module, "BUILDING_BLOCKS",
				OldMaterials.stone()
				.requiresCorrectToolForDrops()
				.strength(4F, 10F)
				.sound(SoundType.STONE));
	}

	@NotNull
	@Override
	public PushReaction getPistonPushReaction(@NotNull BlockState state) {
		return PushReaction.BLOCK;
	}

}
