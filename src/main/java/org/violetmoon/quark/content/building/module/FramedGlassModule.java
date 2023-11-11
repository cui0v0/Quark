package org.violetmoon.quark.content.building.module;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import org.violetmoon.zeta.block.ZetaGlassBlock;
import org.violetmoon.zeta.block.ZetaInheritedPaneBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "building")
public class FramedGlassModule extends ZetaModule {

	@LoadEvent
	public final void register(ZRegister event) {
		Block.Properties props = Block.Properties.of(Material.GLASS)
				.strength(3F, 10F)
				.sound(SoundType.GLASS);
		
		new ZetaInheritedPaneBlock(new ZetaGlassBlock("framed_glass", this, CreativeModeTab.TAB_BUILDING_BLOCKS, false, props));
		
		for(DyeColor dye : DyeColor.values())
			new ZetaInheritedPaneBlock(new ZetaGlassBlock(dye.getName() + "_framed_glass", this, CreativeModeTab.TAB_BUILDING_BLOCKS, true, props));
	}

}
