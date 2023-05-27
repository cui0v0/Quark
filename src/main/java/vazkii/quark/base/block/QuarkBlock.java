package vazkii.quark.base.block;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import vazkii.arl.block.BasicBlock;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.client.handler.RequiredModTooltipHandler;
import vazkii.quark.base.module.QuarkModule;

public class QuarkBlock extends BasicBlock implements IQuarkBlock {

    private final QuarkModule module;
    private BooleanSupplier enabledSupplier = () -> true;

    public QuarkBlock(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties) {
        super(regname, properties);
        this.module = module;

        RegistryHelper.setCreativeTab(this, creativeTab);

        if (module.category.isAddon())
            RequiredModTooltipHandler.map(this, module.category.requiredMod);
    }

    @Override
    public QuarkBlock setCondition(BooleanSupplier enabledSupplier) {
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

    @Nullable
    @SuppressWarnings("unchecked")
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> thisType, BlockEntityType<E> targetType, BlockEntityTicker<? super E> ticker) {
        return targetType == thisType ? (BlockEntityTicker<A>) ticker : null;
    }

    public interface Constructor<T extends Block> {

        T make(String regname, QuarkModule module, CreativeModeTab creativeTab, Properties properties);

    }

}
