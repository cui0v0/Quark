package vazkii.quark.base.item;

import java.util.function.BooleanSupplier;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.Block;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.module.QuarkModule;

public class QuarkSignItem extends SignItem implements IQuarkItem {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkSignItem(QuarkModule module, Block sign, Block wallSign) {
		super(new Item.Properties().stacksTo(16), sign, wallSign);

		RegistryHelper.registerItem(this, IQuarkBlock.inherit(sign, "%s"));
		RegistryHelper.setCreativeTab(this, CreativeModeTabs.BUILDING_BLOCKS);
		
		this.module = module;
	}

	@Override
	public QuarkSignItem setCondition(BooleanSupplier enabledSupplier) {
		this.enabledSupplier = enabledSupplier;
		return this;
	}

	@Override
	public QuarkModule getModule() {
		return module;
	}

	@Override
	public boolean doesConditionApply() {
		return enabledSupplier.getAsBoolean();
	}

}
