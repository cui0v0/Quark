package vazkii.quark.content.world.module;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableSet;

import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import vazkii.quark.base.Quark;
import vazkii.quark.base.block.QuarkLeavesBlock;
import vazkii.quark.base.handler.VariantHandler;
import vazkii.quark.base.handler.WoodSetHandler;
import vazkii.quark.base.handler.WoodSetHandler.WoodSet;
import vazkii.quark.base.handler.advancement.QuarkAdvancementHandler;
import vazkii.quark.base.handler.advancement.QuarkGenericTrigger;
import vazkii.quark.base.handler.advancement.mod.BalancedDietModifier;
import vazkii.zeta.module.ZetaModule;
import vazkii.quark.base.module.config.Config;
import vazkii.quark.base.module.config.Config.Min;
import vazkii.quark.base.module.hint.Hint;
import vazkii.quark.content.world.block.AncientSaplingBlock;
import vazkii.quark.content.world.item.AncientFruitItem;
import vazkii.zeta.event.ZCommonSetup;
import vazkii.zeta.event.ZLootTableLoad;
import vazkii.zeta.event.ZRegister;
import vazkii.zeta.event.bus.LoadEvent;
import vazkii.zeta.event.bus.PlayEvent;
import vazkii.zeta.module.ZetaLoadModule;

@ZetaLoadModule(category = "world")
public class AncientWoodModule extends ZetaModule {

	@Config(flag = "ancient_fruit_xp")
	public static boolean ancientFruitGivesExp = true;

	@Config
	@Min(1)
	public static int ancientFruitExpValue = 10;

	@Config(description = "Set to 0 to disable loot chest generation")
	@Min(0)
	public static int ancientCityLootWeight = 8;

	@Config
	@Min(0)
	public static int ancientCityLootQuality = 1;

	public static WoodSet woodSet;
	public static Block ancient_leaves;
	@Hint public static Block ancient_sapling;
	@Hint public static Item ancient_fruit;

	public static QuarkGenericTrigger ancientFruitTrigger;

	@LoadEvent
	public void setup(ZCommonSetup e) {
		e.enqueueWork(() -> {
			ComposterBlock.COMPOSTABLES.put(ancient_sapling.asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(ancient_leaves.asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.put(ancient_fruit.asItem(), 0.65F);
		});
	}

	@LoadEvent
	public void register(ZRegister event) {
		woodSet = WoodSetHandler.addWoodSet(this, "ancient", MaterialColor.TERRACOTTA_WHITE, MaterialColor.TERRACOTTA_WHITE, true);
		ancient_leaves = new QuarkLeavesBlock(woodSet.name, this, MaterialColor.PLANT);
		ancient_sapling = new AncientSaplingBlock(this);
		ancient_fruit = new AncientFruitItem(this);

		VariantHandler.addFlowerPot(ancient_sapling, Quark.ZETA.registry.getRegistryName(ancient_sapling, Registry.BLOCK).getPath(), Functions.identity());

		QuarkAdvancementHandler.addModifier(new BalancedDietModifier(this, ImmutableSet.of(ancient_fruit)));

		ancientFruitTrigger = QuarkAdvancementHandler.registerGenericTrigger("ancient_fruit_overlevel");
	}

	@PlayEvent
	public void onLootTableLoad(ZLootTableLoad event) {
		int weight = 0;

		if(event.getName().equals(BuiltInLootTables.ANCIENT_CITY))
			weight = ancientCityLootWeight;

		if(weight > 0) {
			LootPoolEntryContainer entry = LootItem.lootTableItem(ancient_sapling)
					.setWeight(weight)
					.setQuality(ancientCityLootQuality)
					.build();
			event.add(entry);
		}
	}

}
