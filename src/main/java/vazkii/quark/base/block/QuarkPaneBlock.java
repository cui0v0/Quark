package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.RenderLayerHandler;
import vazkii.quark.base.handler.RenderLayerHandler.RenderTypeSkeleton;
import vazkii.quark.base.module.QuarkModule;

public class QuarkPaneBlock extends IronBarsBlock implements IQuarkBlock {

	public final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkPaneBlock(String name, QuarkModule module, Block.Properties properties, RenderTypeSkeleton renderType) {
		super(properties);

		this.module = module;
		RegistryHelper.registerBlock(this, name);
		RegistryHelper.setCreativeTab(this, CreativeModeTabs.BUILDING_BLOCKS);

		if(renderType != null)
			RenderLayerHandler.setRenderType(this, renderType);
	}


	@Nullable
	@Override
	public QuarkModule getModule() {
		return module;
	}

	@Override
	public QuarkPaneBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}


}
