package org.violetmoon.zeta.item;

import java.util.function.BooleanSupplier;

import net.minecraft.world.item.Item;
import org.violetmoon.zeta.item.ext.IZetaItemExtensions;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.BooleanSuppliers;

public class ZetaItem extends Item implements IZetaItem, IZetaItemExtensions {
	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = BooleanSuppliers.TRUE;

	public ZetaItem(String regname, ZetaModule module, Properties properties) {
		super(properties);

		this.module = module;
		module.zeta.registry.registerItem(this, regname);

		if(module != null && module.category.isAddon())
			module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);
	}

	@Override
	public ZetaItem setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}
}
