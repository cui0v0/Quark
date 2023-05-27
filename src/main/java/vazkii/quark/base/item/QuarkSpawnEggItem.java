package vazkii.quark.base.item;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.ForgeSpawnEggItem;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.QuarkModule;

public class QuarkSpawnEggItem extends ForgeSpawnEggItem implements IQuarkItem {

	private final QuarkModule module;
	private BooleanSupplier enabledSupplier = () -> true;

	public QuarkSpawnEggItem(Supplier<EntityType<? extends Mob>> type, int primaryColor, int secondaryColor, String regname, QuarkModule module, Properties properties) {
		super(type, primaryColor, secondaryColor, properties);

		RegistryHelper.registerItem(this, regname);
		RegistryHelper.setCreativeTab(this, CreativeModeTabs.TOOLS_AND_UTILITIES);

		this.module = module;
	}

	@Override
	public QuarkSpawnEggItem setCondition(BooleanSupplier enabledSupplier) {
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
