package vazkii.quark.content.building.block;

import net.minecraft.world.item.CreativeModeTab;
import vazkii.quark.base.block.QuarkPillarBlock;
import vazkii.zeta.module.ZetaModule;
import vazkii.quark.content.world.block.IMyaliteColorProvider;

public class MyalitePillarBlock extends QuarkPillarBlock implements IMyaliteColorProvider {

	public MyalitePillarBlock(String regname, ZetaModule module, CreativeModeTab creativeTab, Properties properties) {
		super(regname, module, creativeTab, properties);
	}

}
