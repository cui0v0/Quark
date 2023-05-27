package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import vazkii.arl.interf.IBlockItemProvider;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.RenderLayerHandler;
import vazkii.quark.base.handler.RenderLayerHandler.RenderTypeSkeleton;
import vazkii.quark.base.item.QuarkDoubleHighBlockItem;
import vazkii.quark.base.module.QuarkModule;

public class QuarkDoorBlock extends DoorBlock implements IQuarkBlock, IBlockItemProvider {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkDoorBlock(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties, BlockSetType setType) {
		super(properties, setType);
		this.module = module;

		RenderLayerHandler.setRenderType(this, RenderTypeSkeleton.CUTOUT);
		RegistryHelper.registerBlock(this, regname);
		RegistryHelper.setCreativeTab(this, creativeTab);
	}

	@Override
	public QuarkDoorBlock setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

	@Nullable
	@Override
	public QuarkModule getModule() {
		return module;
	}

	@Override
	public BlockItem provideItemBlock(Block block, Item.Properties props) {
		return new QuarkDoubleHighBlockItem(this, props);
	}

}
