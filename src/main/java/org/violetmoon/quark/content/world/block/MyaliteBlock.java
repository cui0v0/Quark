package org.violetmoon.quark.content.world.block;

import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.block.QuarkBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockColorProvider;

public class MyaliteBlock extends QuarkBlock implements IZetaBlockColorProvider {

	public MyaliteBlock(String regname, ZetaModule module, CreativeModeTab creativeTab, Properties properties) {
		super(regname, module, creativeTab, properties);
	}

	@Override
	public @Nullable String getBlockColorProviderName() {
		return "myalite";
	}

	@Override
	public @Nullable String getItemColorProviderName() {
		return "myalite";
	}

}
