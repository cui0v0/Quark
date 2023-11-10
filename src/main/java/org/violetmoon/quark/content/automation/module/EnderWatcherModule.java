package org.violetmoon.quark.content.automation.module;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.quark.content.automation.block.EnderWatcherBlock;
import org.violetmoon.quark.content.automation.block.be.EnderWatcherBlockEntity;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

@ZetaLoadModule(category = "automation")
public class EnderWatcherModule extends ZetaModule {

	public static BlockEntityType<EnderWatcherBlockEntity> blockEntityType;
	
	public static ManualTrigger watcherCenterTrigger;
	@Hint Block ender_watcher;

	@LoadEvent
	public final void register(ZRegister event) {
		ender_watcher = new EnderWatcherBlock(this);
		blockEntityType = BlockEntityType.Builder.of(EnderWatcherBlockEntity::new, ender_watcher).build(null);
		Quark.ZETA.registry.register(blockEntityType, "ender_watcher", Registry.BLOCK_ENTITY_TYPE_REGISTRY);

		watcherCenterTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("watcher_center");
	}
	
}
