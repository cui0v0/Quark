package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.QuarkModule;

/**
 * @author WireSegal
 * Created at 9:14 PM on 10/8/19.
 */
public class QuarkFenceGateBlock extends FenceGateBlock implements IQuarkBlock {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkFenceGateBlock(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties, WoodType woodType) {
		this(regname, module, creativeTab, properties, woodType.fenceGateOpen(), woodType.fenceGateClose());
	}

	
	public QuarkFenceGateBlock(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties, SoundEvent openSound, SoundEvent closeSound) {
		super(properties, openSound, closeSound);
		this.module = module;

		RegistryHelper.registerBlock(this, regname);
		RegistryHelper.setCreativeTab(this, creativeTab);
	}

	@Override
	public QuarkFenceGateBlock setCondition(BooleanSupplier enabledSupplier) {
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
