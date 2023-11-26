package org.violetmoon.quark.content.building.module;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "building")
public class DuskboundBlocksModule extends ZetaModule {

	@LoadEvent
	public final void register(ZRegister event) {
		event.getVariantRegistry().addSlabAndStairs((IZetaBlock) new ZetaBlock("duskbound_block", this, Block.Properties.copy(Blocks.PURPUR_BLOCK)).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS), null);
		
		new ZetaBlock("duskbound_lantern", this,
				Block.Properties.copy(Blocks.PURPUR_BLOCK)
				.lightLevel(b -> 15)).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
	}
	
}
