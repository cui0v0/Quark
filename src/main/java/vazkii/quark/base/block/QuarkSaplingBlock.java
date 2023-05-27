package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.RenderLayerHandler;
import vazkii.quark.base.handler.RenderLayerHandler.RenderTypeSkeleton;
import vazkii.quark.base.module.QuarkModule;

public abstract class QuarkSaplingBlock extends SaplingBlock implements IQuarkBlock {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;
	
	public QuarkSaplingBlock(String name, QuarkModule module, AbstractTreeGrower tree) {
		super(tree, Block.Properties.copy(Blocks.OAK_SAPLING));
		this.module = module;

		RegistryHelper.registerBlock(this, name + "_sapling");
		RegistryHelper.setCreativeTab(this, CreativeModeTabs.NATURAL_BLOCKS);

		RenderLayerHandler.setRenderType(this, RenderTypeSkeleton.CUTOUT);
	}

	@Override
	public QuarkModule getModule() {
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
