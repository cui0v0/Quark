package org.violetmoon.quark.content.building.module;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.OldMaterials;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "building")
public class SoulSandstoneModule extends ZetaModule {

	@LoadEvent
	public final void register(ZRegister event) {
		Block.Properties props = OldMaterials.stone().mapColor(MapColor.COLOR_BROWN)
				.requiresCorrectToolForDrops()
				.strength(0.8F);
		
		event.getVariantRegistry().addSlabStairsWall((IZetaBlock) new ZetaBlock("soul_sandstone", this, props).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS), null);
		new ZetaBlock("chiseled_soul_sandstone", this, props).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
		event.getVariantRegistry().addSlab((IZetaBlock) new ZetaBlock("cut_soul_sandstone", this, props).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS), null);
		event.getVariantRegistry().addSlabAndStairs((IZetaBlock) new ZetaBlock("smooth_soul_sandstone", this, props).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS), null);
	}
	
}
