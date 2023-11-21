package org.violetmoon.zeta.advancement;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import org.violetmoon.zeta.api.IAdvancementModifier;
import org.violetmoon.zeta.module.ZetaModule;

public abstract class AdvancementModifier implements IAdvancementModifier {

	public final ZetaModule module;
	private BooleanSupplier cond = () -> true;
	
	protected AdvancementModifier(@Nullable ZetaModule module) {
		this.module = module;
	}

	@Override
	public AdvancementModifier setCondition(BooleanSupplier cond) {
		this.cond = cond;
		return this;
	}

	@Override
	public boolean isActive() {
		return (module == null || module.enabled) && cond.getAsBoolean();
	}

}
