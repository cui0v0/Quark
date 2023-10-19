package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.state.properties.WoodType;
import vazkii.quark.base.Quark;
import vazkii.zeta.module.ZetaModule;

public class QuarkStandingSignBlock extends StandingSignBlock implements IQuarkBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkStandingSignBlock(String regname, ZetaModule module, WoodType type, Properties properties) {
		super(properties, type);
		this.module = module;

		Quark.ZETA.registry.registerBlock(this, regname, false);
	}

	@Override
	public QuarkStandingSignBlock setCondition(BooleanSupplier enabledSupplier) {
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
