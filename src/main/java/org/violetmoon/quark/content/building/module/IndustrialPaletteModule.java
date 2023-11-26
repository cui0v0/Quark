package org.violetmoon.quark.content.building.module;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.util.ForgeSoundType;
import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.content.building.block.VariantLadderBlock;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.block.ZetaPillarBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.function.BooleanSupplier;

@ZetaLoadModule(category = "building")
public class IndustrialPaletteModule extends ZetaModule {

	private static final SoundType IRON_LADDER_SOUND_TYPE = new ForgeSoundType(1.0F, 1.0F,
			() -> SoundEvents.METAL_BREAK,
			() -> SoundEvents.LADDER_STEP,
			() -> SoundEvents.METAL_PLACE,
			() -> SoundEvents.METAL_HIT,
			() -> SoundEvents.LADDER_FALL);

	@Config(flag = "iron_plates")
	public static boolean enableIronPlates = true;

	@Config(flag = "iron_ladder")
	public static boolean enableIronLadder = true;

	@LoadEvent
	public final void register(ZRegister event) {
		Block.Properties props = Block.Properties.copy(Blocks.IRON_BLOCK);

		BooleanSupplier ironPlateCond = () -> enableIronPlates;
		BooleanSupplier ironLadderCond = () -> enableIronLadder;

		event.getVariantRegistry().addSlabAndStairs((IZetaBlock) new ZetaBlock("iron_plate", this, props).setCondition(ironPlateCond).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS), null);
		event.getVariantRegistry().addSlabAndStairs((IZetaBlock) new ZetaBlock("rusty_iron_plate", this, props).setCondition(ironPlateCond).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS), null);

		new ZetaPillarBlock("iron_pillar", this, props).setCondition(ironPlateCond).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);;

		new VariantLadderBlock("iron", this, Block.Properties.of()
				.noCollission()
				.strength(0.8F)
				.sound(IRON_LADDER_SOUND_TYPE)
				.noOcclusion()
				.pushReaction(PushReaction.DESTROY), false
		)
		.setCondition(ironLadderCond);
	}

}
