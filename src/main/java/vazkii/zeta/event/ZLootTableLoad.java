package vazkii.zeta.event;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import vazkii.quark.mixin.zeta.AccessorLootTable;
import vazkii.zeta.event.bus.Cancellable;
import vazkii.zeta.event.bus.IZetaPlayEvent;

public interface ZLootTableLoad extends IZetaPlayEvent, Cancellable {
	ResourceLocation getName();
	LootTable getTable();
	LootTables getLootTableManager();

	default void add(LootPoolEntryContainer entry) {
		LootTable table = getTable();

		List<LootPool> pools = ((AccessorLootTable) table).zeta$getPools();
		if (pools != null && !pools.isEmpty()) {
			LootPool firstPool = pools.get(0);
			LootPoolEntryContainer[] entries = firstPool.entries;

			LootPoolEntryContainer[] newEntries = new LootPoolEntryContainer[entries.length + 1];
			System.arraycopy(entries, 0, newEntries, 0, entries.length);

			newEntries[entries.length] = entry;
			firstPool.entries = newEntries;
		}
	}
}
