package org.violetmoon.quark.content.building.module;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Material;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.block.ZetaPillarBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZLoadComplete;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "building")
public class MidoriModule extends ZetaModule {

	private static Item moss_paste;
	
	@LoadEvent
	public final void register(ZRegister event) {
		moss_paste = new ZetaItem("moss_paste", this, new Item.Properties());
		
		Block.Properties props = Block.Properties.of(Material.STONE, MapColor.COLOR_LIGHT_GREEN)
				.requiresCorrectToolForDrops()
				.strength(1.5F, 6.0F);

		event.getVariantRegistry().addSlabAndStairs(new ZetaBlock("midori_block", this, "BUILDING_BLOCKS", props));
		new ZetaPillarBlock("midori_pillar", this, "BUILDING_BLOCKS", props);
	}

	@LoadEvent
	public void loadComplete(ZLoadComplete event) {
		event.enqueueWork(() -> {
			ComposterBlock.COMPOSTABLES.put(moss_paste, 0.5F);
		});
	}
	
}
