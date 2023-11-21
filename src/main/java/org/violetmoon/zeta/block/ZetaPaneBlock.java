package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
		module.zeta.registry.setCreativeTab(this, CreativeModeTab.TAB_DECORATIONS);

		if(renderLayer != null)
			module.zeta.renderLayerRegistry.put(this, renderLayer);
	}

	@Override
	public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
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
