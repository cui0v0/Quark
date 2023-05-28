package vazkii.quark.content.building.module;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkPillarBlock;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.building.block.MudBrickLatticeBlock;

@LoadModule(category = ModuleCategory.BUILDING)
public class MoreMudBlocksModule extends QuarkModule {

	@Override
	public void register() {
		BlockBehaviour.Properties props = Properties.copy(Blocks.MUD_BRICKS);
		
		new QuarkBlock("carved_mud_bricks", this, CreativeModeTabs.BUILDING_BLOCKS, props);
		new QuarkPillarBlock("mud_pillar", this, CreativeModeTabs.BUILDING_BLOCKS, props);
		new MudBrickLatticeBlock(this, props);
	}
	
}
