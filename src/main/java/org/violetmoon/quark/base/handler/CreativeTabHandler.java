package org.violetmoon.quark.base.handler;

import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.block.IQuarkBlock;

//TODO: creative tab registration is changing in 1.20 anyway
public class CreativeTabHandler {

    public static void addTab(IQuarkBlock block, @Nullable CreativeModeTab creativeTab) {
        Quark.ZETA.registry.setCreativeTab(block.getBlock(), creativeTab, block::isEnabled);
    }

}
