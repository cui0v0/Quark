package org.violetmoon.zeta.block;

import javax.annotation.Nonnull;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.CreativeModeTab;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaWoodenButtonBlock extends ZetaButtonBlock {

	public ZetaWoodenButtonBlock(String regname, ZetaModule module, Properties properties) {
		super(regname, module, CreativeModeTab.TAB_REDSTONE, properties);
	}

	@Nonnull
	@Override
	protected SoundEvent getSound(boolean powered) {
		return powered ? SoundEvents.WOODEN_BUTTON_CLICK_ON : SoundEvents.WOODEN_BUTTON_CLICK_OFF;
	}

	@Override
	public int getPressDuration() {
		return 30;
	}

}
