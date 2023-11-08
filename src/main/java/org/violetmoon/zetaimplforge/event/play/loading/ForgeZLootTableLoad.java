package org.violetmoon.zetaimplforge.event.play.loading;

import org.violetmoon.zeta.event.play.loading.ZLootTableLoad;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraftforge.event.LootTableLoadEvent;

public record ForgeZLootTableLoad(LootTableLoadEvent e) implements ZLootTableLoad {
	@Override
	public ResourceLocation getName() {
		return e.getName();
	}

	@Override
	public LootTable getTable() {
		return e.getTable();
	}

	@Override
	public LootTables getLootTableManager() {
		return e.getLootTableManager();
	}

	@Override
	public boolean isCanceled() {
		return e.isCanceled();
	}

	@Override
	public void setCanceled(boolean cancel) {
		e.setCanceled(cancel);
	}
}
