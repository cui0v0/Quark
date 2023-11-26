package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.PressurePlateBlock;
import org.violetmoon.zeta.module.ZetaModule;

/**
 * @author WireSegal
 * Created at 9:41 PM on 10/8/19.
 */
public class ZetaPressurePlateBlock extends PressurePlateBlock implements IZetaBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaPressurePlateBlock(Sensitivity sensitivity, String regname, ZetaModule module, String creativeTab, Properties properties, BlockSetType blockSetType) {
		super(sensitivity, properties, blockSetType);
		this.module = module;

		module.zeta.registry.registerBlock(this, regname, true);
		setCreativeTab(CreativeModeTabs.REDSTONE_BLOCKS);
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
