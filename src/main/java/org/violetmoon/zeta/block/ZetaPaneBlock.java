package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.RenderLayerRegistry.Layer;

public class ZetaPaneBlock extends IronBarsBlock implements IZetaBlock {

	public final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public ZetaPaneBlock(String name, ZetaModule module, Block.Properties properties, Layer renderLayer) {
		super(properties);

		this.module = module;
		module.zeta.registry.registerBlock(this, name, true);
		setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);

		if(renderLayer != null)
			module.zeta.renderLayerRegistry.put(this, renderLayer);
	}

	@Nullable
	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public ZetaPaneBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}


}
