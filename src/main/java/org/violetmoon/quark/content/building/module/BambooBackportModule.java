package org.violetmoon.quark.content.building.module;

import org.violetmoon.quark.base.block.QuarkBlock;
import org.violetmoon.quark.base.handler.WoodSetHandler;
import org.violetmoon.quark.base.handler.WoodSetHandler.WoodSet;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

@ZetaLoadModule(category = "building")
public class BambooBackportModule extends ZetaModule {

	public static WoodSet woodSet;

	@LoadEvent
	public final void register(ZRegister event) {
		woodSet = WoodSetHandler.addWoodSet(this, "bamboo", MaterialColor.COLOR_YELLOW, MaterialColor.COLOR_YELLOW, false, false, true);

		new QuarkBlock("bamboo_mosaic", this, CreativeModeTab.TAB_BUILDING_BLOCKS, BlockBehaviour.Properties.copy(woodSet.planks));
	}

}
