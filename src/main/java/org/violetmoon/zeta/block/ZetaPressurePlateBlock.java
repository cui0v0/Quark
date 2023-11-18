package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PressurePlateBlock;
import org.violetmoon.zeta.module.ZetaModule;

/**
 * @author WireSegal
 * Created at 9:41 PM on 10/8/19.
 */
public class ZetaPressurePlateBlock extends PressurePlateBlock implements IZetaBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaPressurePlateBlock(Sensitivity sensitivity, String regname, ZetaModule module, CreativeModeTab creativeTab, Properties properties, BlockSetType blockSetType) {
		super(sensitivity, properties, blockSetType);
		this.module = module;

		module.zeta.registry.registerBlock(this, regname, true);
		module.zeta.registry.setCreativeTab(this, creativeTab);
	}

	@Override
	public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
	}

	@Override
	public ZetaPressurePlateBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return module;
	}

}
