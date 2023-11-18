package org.violetmoon.quark.content.building.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.ZetaSlabBlock;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class QuarkVerticalSlabBlock extends VerticalSlabBlock implements IZetaBlock {

    private final ZetaModule module;
    private BooleanSupplier enabledSupplier = () -> true;

    public QuarkVerticalSlabBlock(Block parent, ZetaModule module) {
        super(() -> parent, Block.Properties.copy(parent));
        String resloc = Quark.ZETA.registryUtil.inherit(parent, s -> s.replace("_slab", "_vertical_slab"));
	    Quark.ZETA.registry.registerBlock(this, resloc, true);

	    this.module = module;

        module.zeta.registry.setCreativeTab(this, CreativeModeTab.TAB_BUILDING_BLOCKS);

        if (module.category.isAddon())
            module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);


        if (!(parent instanceof SlabBlock))
            throw new IllegalArgumentException("Can't rotate a non-slab block into a vertical slab.");

        if (parent instanceof ZetaSlabBlock quarkSlab)
            setCondition(quarkSlab.parent::isEnabled);
    }


    @Override
    public void fillItemCategory(@NotNull CreativeModeTab group, @NotNull NonNullList<ItemStack> items) {
        if (isEnabled() || group == CreativeModeTab.TAB_SEARCH)
            super.fillItemCategory(group, items);
    }

    @Override
    public QuarkVerticalSlabBlock setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return enabledSupplier.getAsBoolean();
    }

    @Nullable
    @Override
    public ZetaModule getModule() {
        return module;
    }

}
