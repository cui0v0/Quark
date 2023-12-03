package org.violetmoon.zeta.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

import java.util.function.BooleanSupplier;

public class ZetaHangingSignItem extends HangingSignItem implements IZetaItem {
    private final ZetaModule module;
    private BooleanSupplier enabledSupplier = () -> true;

    public ZetaHangingSignItem(ZetaModule module, Block sign, Block wallSign) {
        super(sign, wallSign, new Item.Properties().stacksTo(16));

        String resloc = module.zeta.registryUtil.inherit(sign, "%s");
        module.zeta.registry.registerItem(this, resloc);
        CreativeTabManager.addToCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, this);
        this.module = module;
    }

    @Override
    public ZetaHangingSignItem setCondition(BooleanSupplier enabledSupplier) {
        this.enabledSupplier = enabledSupplier;
        return this;
    }

    @Override
    public ZetaModule getModule() {
        return module;
    }

    @Override
    public boolean doesConditionApply() {
        return enabledSupplier.getAsBoolean();
    }
}
