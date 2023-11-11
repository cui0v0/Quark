package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.module.ZetaModule;

// Wrapper to allow vanilla blocks to be treated as zeta blocks contextualized under a module
public class ZetaBlockWrapper implements IZetaBlock {

	private final Block parent;
	private final ZetaModule module;
	
	private BooleanSupplier condition;
	
	public ZetaBlockWrapper(Block parent, ZetaModule module) {
		this.parent = parent;
		this.module = module;
	}
	
	@Override
	public Block getBlock() {
		return parent;
	}

	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public ZetaBlockWrapper setCondition(BooleanSupplier condition) {
		this.condition = condition;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return condition == null || condition.getAsBoolean();
	}

}
