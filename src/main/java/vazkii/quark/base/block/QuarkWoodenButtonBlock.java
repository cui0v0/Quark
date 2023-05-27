package vazkii.quark.base.block;

import javax.annotation.Nonnull;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import vazkii.quark.base.module.QuarkModule;

public class QuarkWoodenButtonBlock extends QuarkButtonBlock {

	public QuarkWoodenButtonBlock(String regname, QuarkModule module, Properties properties, BlockSetType blockSet) {
		super(regname, module, CreativeModeTabs.REDSTONE_BLOCKS, properties, blockSet, 30, true);
	}

	@Nonnull
	@Override
	protected SoundEvent getSound(boolean powered) {
		return powered ? SoundEvents.WOODEN_BUTTON_CLICK_ON : SoundEvents.WOODEN_BUTTON_CLICK_OFF;
	}

}
