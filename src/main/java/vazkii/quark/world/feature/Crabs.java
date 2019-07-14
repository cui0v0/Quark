package vazkii.quark.world.feature;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Biomes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.quark.base.Quark;
import vazkii.quark.base.item.ItemQuarkFood;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.lib.LibPotionIndices;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.potion.PotionMod;
import vazkii.quark.misc.feature.ExtraPotions;
import vazkii.quark.world.entity.EntityCrab;
import vazkii.quark.world.item.ItemCrabShell;

public class Crabs extends Feature {

	public static int weight, min, max;

	public static ItemQuarkFood crabLeg;
	public static ItemQuarkFood cookedCrabLeg;
	public static ItemMod crabShell;

	public static PotionMod resilience;

	@Override
	public void setupConfig() {
		weight = loadPropInt("Spawn Weight", "The higher, the more will spawn", 40);
		min = loadPropInt("Smallest spawn group", "", 1);
		max = loadPropInt("Largest spawn group", "", 3);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		crabLeg = new ItemQuarkFood("crab_leg", 1, 0.3F, true);
		cookedCrabLeg = new ItemQuarkFood("cooked_crab_leg", 9, 0.8F, true);
		crabShell = new ItemCrabShell();

		resilience = new PotionMod("resilience", false, 0x893c22, LibPotionIndices.RESILIENCE);
		resilience.registerPotionAttributeModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "0cb68666-1b17-42b3-82c0-28412d6c0b22", 0.5, 0);

		String crabName = "quark:crab";
		LootTableList.register(EntityCrab.CRAB_LOOT_TABLE);
		EntityRegistry.registerModEntity(new ResourceLocation(crabName), EntityCrab.class, crabName, LibEntityIDs.CRAB, Quark.instance, 80, 3, true, 0x893c22, 0x916548);
	}

	@Override
	public void postPreInit() {
		ExtraPotions.addStandardBlend(resilience, crabShell);
	}

	@Override
	public void init() {
		EntityRegistry.addSpawn(EntityCrab.class, weight, min, max, EnumCreatureType.CREATURE, Biomes.BEACH);
	}

	@Override
	public void postInit() {
		FurnaceRecipes.instance().addSmelting(crabLeg, new ItemStack(cookedCrabLeg), 0.35F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient() {
//		RenderingRegistry.registerEntityRenderingHandler(EntityCrab.class, RenderCrab.FACTORY);
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

}
