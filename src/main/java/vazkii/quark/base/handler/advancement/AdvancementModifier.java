package vazkii.quark.base.handler.advancement;

import javax.annotation.Nullable;

import com.google.common.base.Supplier;

import vazkii.quark.api.IAdvancementModifier;
import vazkii.zeta.module.ZetaModule;

public abstract class AdvancementModifier implements IAdvancementModifier {

	public final ZetaModule module;
	private Supplier<Boolean> cond;
	
	protected AdvancementModifier(@Nullable ZetaModule module) {
		this.module = module;
	}

	@Override
	public AdvancementModifier setCondition(Supplier<Boolean> cond) {
		this.cond = cond;
		return this;
	}

	@Override
	public boolean isActive() {
		return (module == null || module.enabled) && (cond == null || cond.get());
	}

}
