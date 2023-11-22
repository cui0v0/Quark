package org.violetmoon.zeta.block;

import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.violetmoon.zeta.module.ZetaModule;

public class ZetaBlock extends Block implements IZetaBlock {

    private final ZetaModule module;
    private BooleanSupplier enabledSupplier = () -> true;

    public ZetaBlock(String regname, ZetaModule module, String creativeTab, Properties properties) {
        super(properties);

        this.module = module;
        module.zeta.registry.registerBlock(this, regname);
        module.zeta.registry.setCreativeTab(this, creativeTab);

        if (module.category.isAddon())
            module.zeta.requiredModTooltipHandler.map(this, module.category.requiredMod);
    }

    @Override
    public ZetaBlock setCondition(BooleanSupplier enabledSupplier) {
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

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> thisType, BlockEntityType<E> targetType, BlockEntityTicker<? super E> ticker) {
        return targetType == thisType ? (BlockEntityTicker<A>) ticker : null;
    }

    public interface Constructor<T extends Block> {

        T make(String regname, ZetaModule module, CreativeModeTab creativeTab, Properties properties);

    }

}
