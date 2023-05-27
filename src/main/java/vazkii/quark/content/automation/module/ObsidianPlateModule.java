package vazkii.quark.content.automation.module;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.module.QuarkModule;
import vazkii.quark.content.automation.block.ObsidianPressurePlateBlock;

/**
 * @author WireSegal
 * Created at 9:51 PM on 10/8/19.
 */
@LoadModule(category = ModuleCategory.AUTOMATION)
public class ObsidianPlateModule extends QuarkModule {
	@Override
	public void register() {
		BlockSetType obsidianSet = BlockSetType.register(new BlockSetType(Quark.MOD_ID + ":obsidian", SoundType.METAL, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON));

		new ObsidianPressurePlateBlock("obsidian_pressure_plate", this, CreativeModeTabs.REDSTONE_BLOCKS,
				Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)
				.requiresCorrectToolForDrops()
				.noCollission()
				.strength(2F, 1200.0F), obsidianSet);
	}
}
