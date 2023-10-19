package vazkii.quark.content.building.module;

import net.minecraft.world.level.block.Block;
import vazkii.quark.base.module.LoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.quark.base.module.hint.Hint;
import vazkii.quark.content.building.block.SturdyStoneBlock;
import vazkii.zeta.event.ZRegister;
import vazkii.zeta.event.bus.LoadEvent;

@LoadModule(category = "building")
public class SturdyStoneModule extends ZetaModule {

	@Hint Block sturdy_stone;
	
	@LoadEvent
	public final void register(ZRegister event) {
		sturdy_stone = new SturdyStoneBlock(this);
	}
	
}
