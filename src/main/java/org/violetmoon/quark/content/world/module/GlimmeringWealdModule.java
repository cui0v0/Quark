package org.violetmoon.quark.content.world.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.base.handler.GeneralConfig;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.base.handler.UndergroundBiomeHandler;
import org.violetmoon.quark.base.util.registryaccess.RegistryAccessUtil;
import org.violetmoon.quark.content.mobs.module.StonelingsModule;
import org.violetmoon.quark.content.world.block.GlowLichenGrowthBlock;
import org.violetmoon.quark.content.world.block.GlowShroomBlock;
import org.violetmoon.quark.content.world.block.GlowShroomRingBlock;
import org.violetmoon.quark.content.world.block.HugeGlowShroomBlock;
import org.violetmoon.quark.content.world.feature.GlowExtrasFeature;
import org.violetmoon.quark.content.world.feature.GlowShroomsFeature;
import org.violetmoon.zeta.advancement.modifier.AdventuringTimeModifier;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.Hint;

import java.util.List;

@ZetaLoadModule(category = "world")
public class GlimmeringWealdModule extends ZetaModule {

	private static final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);
	public static final ResourceLocation BIOME_NAME = new ResourceLocation(Quark.MOD_ID, "glimmering_weald");
	public static final ResourceKey<Biome> BIOME_KEY = ResourceKey.create(Registries.BIOME, BIOME_NAME);

	public static final BootstapContext<PlacedFeature> bootstapContext = new RegistrySetBuilder().createState(RegistryAccessUtil.getRegistryAccess()).bootstapContext();
	public static final BootstapContext<ConfiguredFeature<?,?>> bootstapContextConfig = new RegistrySetBuilder().createState(RegistryAccessUtil.getRegistryAccess()).bootstapContext();
	public static final BootstapContext<ConfiguredWorldCarver<?>> bootstapContextWorld = new RegistrySetBuilder().createState(RegistryAccessUtil.getRegistryAccess()).bootstapContext();


	static {
	}

	public static final Holder<PlacedFeature> ORE_LAPIS_EXTRA = bootstapContext.register(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation("quark", "ore_lapis_glimmering_weald")), new PlacedFeature(bootstapContext.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(OreFeatures.ORE_LAPIS), OrePlacements.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(0)))));
	public static Holder<PlacedFeature> placed_glow_shrooms;
	public static Holder<PlacedFeature> placed_glow_extras;

	@Hint public static Block glow_shroom;
	@Hint public static Block glow_lichen_growth;
	public static Block glow_shroom_block;
	public static Block glow_shroom_stem;
	public static Block glow_shroom_ring;

	public static TagKey<Item> glowShroomFeedablesTag;

	@Config(name = "Min Depth Range",
			description = "Experimental, dont change if you dont know what you are doing. Depth min value from which biome will spawn. Decreasing will make biome appear more often")
	@Config.Min(-2)
	@Config.Max(2)
	public static double minDepthRange = 1.55F;
	@Config(name = "Max Weirdness Range",
			description = "Experimental, dont change if you dont know what you are doing. Depth max value until which biome will spawn. Increasing will make biome appear more often")
	@Config.Min(-2)
	@Config.Max(2)
	public static double maxDepthRange = 2;

	@LoadEvent
	public final void register(ZRegister event) {
		CreativeTabManager.daisyChain();
		glow_shroom = new GlowShroomBlock(this).setCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, Blocks.HANGING_ROOTS, false);
		glow_shroom_block = new HugeGlowShroomBlock("glow_shroom_block", this, true);
		glow_shroom_stem = new HugeGlowShroomBlock("glow_shroom_stem", this, false);
		glow_shroom_ring = new GlowShroomRingBlock(this);
		glow_lichen_growth = new GlowLichenGrowthBlock(this);
		CreativeTabManager.endDaisyChain();

		event.getVariantRegistry().addFlowerPot(glow_lichen_growth, "glow_lichen_growth", prop -> prop.lightLevel((state) -> 8));
		event.getVariantRegistry().addFlowerPot(glow_shroom, "glow_shroom", prop -> prop.lightLevel((state) -> 10));

		//fixme
		makeFeatures();
	}

	@LoadEvent
	public void postRegister(ZRegister.Post e) {
		//fixme
		Quark.ZETA.registry.register(makeBiome(), BIOME_NAME, Registries.BIOME);
		float wmin = (float) minDepthRange;
		float wmax = (float) maxDepthRange;
		if(wmin >= wmax){
			Quark.LOG.warn("Incorrect value for Glimmering Weald biome parameters. Using default");
			wmax = 2;
			wmin = 1.55f;
		}
		UndergroundBiomeHandler.addUndergroundBiome(this, Climate.parameters(FULL_RANGE, FULL_RANGE, FULL_RANGE, FULL_RANGE,
				Climate.Parameter.span(wmin, wmax), FULL_RANGE, 0F), BIOME_NAME);

		Quark.ZETA.advancementModifierRegistry.addModifier(new AdventuringTimeModifier(this, ImmutableSet.of(BIOME_KEY))
			.setCondition(() -> GeneralConfig.enableAdvancementModification));
	}

	@LoadEvent
	public void setup(ZCommonSetup e) {
		glowShroomFeedablesTag = ItemTags.create(new ResourceLocation(Quark.MOD_ID, "glow_shroom_feedables"));

		e.enqueueWork(() -> {
			ComposterBlock.COMPOSTABLES.put(glow_shroom.asItem(), 0.65F);
			ComposterBlock.COMPOSTABLES.put(glow_shroom_block.asItem(), 0.65F);
			ComposterBlock.COMPOSTABLES.put(glow_shroom_stem.asItem(), 0.65F);
			ComposterBlock.COMPOSTABLES.put(glow_shroom_ring.asItem(), 0.65F);

			ComposterBlock.COMPOSTABLES.put(glow_lichen_growth.asItem(), 0.5F);
		});
	}

	//fixme
	private static void makeFeatures() {
		placed_glow_shrooms = place("glow_shrooms", new GlowShroomsFeature(), GlowShroomsFeature.placed());
		placed_glow_extras = place("glow_extras", new GlowExtrasFeature(), GlowExtrasFeature.placed());
	}


	private static Holder<PlacedFeature> place(String featureName, Feature<NoneFeatureConfiguration> feature, List<PlacementModifier> placer) {
		String name = Quark.MOD_ID + ":" + featureName;

		Quark.ZETA.registry.register(feature, name, Registries.FEATURE);
		Holder<ConfiguredFeature<?, ?>> configured = bootstapContextConfig.register(ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(name)), new ConfiguredFeature<>(feature, NoneFeatureConfiguration.NONE));
		return bootstapContext.register(ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(name)), new PlacedFeature(configured, placer));
	}


	private static Biome makeBiome() {
		MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
		BiomeDefaultFeatures.commonSpawns(mobs);

		if(Quark.ZETA.modules.isEnabled(StonelingsModule.class))
			mobs.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(StonelingsModule.stonelingType, 200, 1, 4));
		mobs.addSpawn(MobCategory.UNDERGROUND_WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 20, 4, 6));

		BiomeGenerationSettings.Builder settings = new BiomeGenerationSettings.Builder(bootstapContext.lookup(Registries.PLACED_FEATURE), bootstapContextWorld.lookup(Registries.CONFIGURED_CARVER));
		OverworldBiomes.globalOverworldGeneration(settings);
		BiomeDefaultFeatures.addPlainGrass(settings);
		BiomeDefaultFeatures.addDefaultOres(settings, true);
		BiomeDefaultFeatures.addDefaultSoftDisks(settings);
		BiomeDefaultFeatures.addPlainVegetation(settings);
		BiomeDefaultFeatures.addDefaultMushrooms(settings);
		BiomeDefaultFeatures.addDefaultExtraVegetation(settings);

		settings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, placed_glow_shrooms);
		settings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, placed_glow_extras);

		settings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_LAPIS_EXTRA);

		Music music = Musics.createGameMusic(Holder.direct(QuarkSounds.MUSIC_GLIMMERING_WEALD));
		Biome biome = OverworldBiomes.biome(true, 0.8F, 0.4F, mobs, settings, music);

		return biome;
	}

}
