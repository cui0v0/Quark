package vazkii.quark.base.handler;

import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;
import vazkii.quark.base.Quark;
import vazkii.quark.base.block.IQuarkBlock;

//TODO: creative tab registration is changing in 1.20 anyway
public class CreativeTabHandler {

    public static void addTab(IQuarkBlock block, @Nullable CreativeModeTab creativeTab) {
        Quark.ZETA.registry.setCreativeTab(block.getBlock(), creativeTab, block::isEnabled);
    }

}
