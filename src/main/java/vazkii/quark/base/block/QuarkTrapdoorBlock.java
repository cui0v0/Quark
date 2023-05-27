package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.handler.RenderLayerHandler;
import vazkii.quark.base.handler.RenderLayerHandler.RenderTypeSkeleton;
import vazkii.quark.base.module.QuarkModule;

public class QuarkTrapdoorBlock extends TrapDoorBlock implements IQuarkBlock {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkTrapdoorBlock(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties, BlockSetType setType) {
		super(properties, setType);
		this.module = module;

		RenderLayerHandler.setRenderType(this, RenderTypeSkeleton.CUTOUT);
		RegistryHelper.registerBlock(this, regname);

		RegistryHelper.setCreativeTab(this, creativeTab);

	}

	@Override
	public QuarkTrapdoorBlock setCondition(BooleanSupplier enabledSupplier) {
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

}
