package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.QuarkModule;

/**
 * @author WireSegal
 * Created at 9:14 PM on 10/8/19.
 */
public abstract class QuarkButtonBlock extends ButtonBlock implements IQuarkBlock {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkButtonBlock(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties, BlockSetType setType, int ticks, boolean allowArrows) {
		super(properties, setType, ticks, allowArrows);
		this.module = module;

		RegistryHelper.registerBlock(this, regname);
		RegistryHelper.setCreativeTab(this, creativeTab);
	}

	@Override
	public QuarkButtonBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public QuarkModule getModule() {
		return module;
	}

}
