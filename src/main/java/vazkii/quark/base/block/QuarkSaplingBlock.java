package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nonnull;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import vazkii.quark.base.handler.CreativeTabHandler;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.registry.RenderLayerRegistry;

public abstract class QuarkSaplingBlock extends SaplingBlock implements IQuarkBlock {

	private final ZetaModule module;
	private BooleanSupplier enabledSupplier = () -> true;
	
	public QuarkSaplingBlock(String name, ZetaModule module, AbstractTreeGrower tree) {
		super(tree, Block.Properties.copy(Blocks.OAK_SAPLING));
		this.module = module;

		module.zeta.registry.registerBlock(this, name + "_sapling", true);
		CreativeTabHandler.addTab(this, CreativeModeTab.TAB_DECORATIONS);

		module.zeta.renderLayerRegistry.put(this, RenderLayerRegistry.Layer.CUTOUT);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab group, @Nonnull NonNullList<ItemStack> items) {
		if(isEnabled() || group == CreativeModeTab.TAB_SEARCH)
			super.fillItemCategory(group, items);
	}

	@Override
	public ZetaModule getModule() {
		return module;
	}

	@Override
	public QuarkSaplingBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}
	
}
