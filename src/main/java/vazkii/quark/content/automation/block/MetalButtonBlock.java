package vazkii.quark.content.automation.block;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.Material;
import vazkii.quark.base.block.QuarkButtonBlock;
import vazkii.quark.base.module.QuarkModule;

/**
 * @author WireSegal
 * Created at 9:14 PM on 10/8/19.
 */
public class MetalButtonBlock extends QuarkButtonBlock {

	public MetalButtonBlock(String regname, QuarkModule module, BlockSetType setType, int speed) {
		super(regname, module, CreativeModeTabs.REDSTONE_BLOCKS,
				Block.Properties.of(Material.DECORATION)
						.noCollission()
						.strength(0.5F)
						.sound(SoundType.METAL), setType, speed, false);
	}
}
