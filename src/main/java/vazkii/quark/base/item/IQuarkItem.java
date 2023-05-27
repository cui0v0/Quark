package vazkii.quark.base.item;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.CreativeModeTabEvent.BuildContents;
import vazkii.arl.interf.ICreativeExtras;
import vazkii.quark.base.handler.GeneralConfig;
import vazkii.quark.base.module.QuarkModule;

public interface IQuarkItem extends ICreativeExtras {

	@Nullable
	QuarkModule getModule();

	default IQuarkItem setCondition(BooleanSupplier condition) {
		return this;
	}

	default boolean doesConditionApply() {
		return true;
	}
	
	default Item getItem() {
		return (Item) this;
	}

	default boolean isEnabled() {
		QuarkModule module = getModule();
		return module != null && module.enabled && doesConditionApply();
	}
	
	@Override
	default boolean canAddToCreativeTab(CreativeModeTab tab) {
		return !GeneralConfig.hideDisabledContent || isEnabled();
	}
	
	@Override
	default void addCreativeModeExtras(CreativeModeTab tab, BuildContents event) { }
	
}
