package org.violetmoon.zeta.block;

import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.function.BooleanSupplier;

public class ZetaCeilingHangingSignBlock extends CeilingHangingSignBlock implements IZetaBlock {
    private final ZetaModule module;
    private BooleanSupplier enabledSupplier = () -> true;

    public ZetaCeilingHangingSignBlock(String regname, ZetaModule module, WoodType type, BlockBehaviour.Properties properties) {
        super(properties, type);
        this.module = module;

        module.zeta.registry.registerBlock(this, regname, false);
    }

    @Override
    public ZetaCeilingHangingSignBlock setCondition(BooleanSupplier enabledSupplier) {
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
