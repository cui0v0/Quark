package vazkii.quark.content.building.block;

import net.minecraft.world.item.CreativeModeTab;
import org.jetbrains.annotations.Nullable;
import vazkii.quark.base.block.QuarkPillarBlock;
import vazkii.zeta.module.ZetaModule;
import vazkii.zeta.registry.IZetaBlockColorProvider;

public class MyalitePillarBlock extends QuarkPillarBlock implements IZetaBlockColorProvider {

	public MyalitePillarBlock(String regname, ZetaModule module, CreativeModeTab creativeTab, Properties properties) {
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
