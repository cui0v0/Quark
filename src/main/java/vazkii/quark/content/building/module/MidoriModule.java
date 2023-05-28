package vazkii.quark.content.building.module;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkPillarBlock;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;

@LoadModule(category = ModuleCategory.BUILDING)
public class MidoriModule extends QuarkModule {

	@Override
	public void register() {
		Item moss_paste = new QuarkItem("moss_paste", this, new Item.Properties());
		RegistryHelper.setCreativeTab(moss_paste, CreativeModeTabs.INGREDIENTS);
		
		Block.Properties props = Block.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GREEN)
				.requiresCorrectToolForDrops()
				.strength(1.5F, 6.0F);
		
		VariantHandler.addSlabAndStairs(new QuarkBlock("midori_block", this, CreativeModeTabs.BUILDING_BLOCKS, props));
		new QuarkPillarBlock("midori_pillar", this, CreativeModeTabs.BUILDING_BLOCKS, props);
	}
	
}
