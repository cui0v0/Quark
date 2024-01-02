package org.violetmoon.quark.content.world.module;

import com.google.common.base.Functions;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.base.handler.WoodSetHandler;
import org.violetmoon.quark.base.handler.WoodSetHandler.WoodSet;
import org.violetmoon.quark.base.world.WorldGenHandler;
import org.violetmoon.quark.base.world.WorldGenWeights;
import org.violetmoon.quark.content.world.block.BlossomLeavesBlock;
import org.violetmoon.quark.content.world.config.BlossomTreeConfig;
import org.violetmoon.quark.content.world.gen.BlossomTreeGenerator;
import org.violetmoon.zeta.block.ZetaSaplingBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.world.PassthruTreeGrower;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

@ZetaLoadModule(category = "world")
public class BlossomTreesModule extends ZetaModule {

	public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_BLOSSOM_KEY = registerKey("blue_blossom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> LAVENDER_BLOSSOM_KEY = registerKey("lavender_blossom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_BLOSSOM_KEY = registerKey("orange_blossom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_BLOSSOM_KEY = registerKey("yellow_blossom");
	public static final ResourceKey<ConfiguredFeature<?, ?>> RED_BLOSSOM_KEY = registerKey("red_blossom");

	@Config
	public BlossomTreeConfig blue = new BlossomTreeConfig(200, Tags.Biomes.IS_SNOWY);
	@Config
	public BlossomTreeConfig lavender = new BlossomTreeConfig(100, Tags.Biomes.IS_SWAMP);
	@Config
	public BlossomTreeConfig orange = new BlossomTreeConfig(100, BiomeTags.IS_SAVANNA);
	@Config
	public BlossomTreeConfig yellow = new BlossomTreeConfig(200, Tags.Biomes.IS_PLAINS);
	@Config
	public BlossomTreeConfig red = new BlossomTreeConfig(30, BiomeTags.IS_BADLANDS);

	@Config
	public static boolean dropLeafParticles = true;

	public static WoodSet woodSet;

	public static final List<BlossomTree> blossomTrees = new ArrayList<>(5);

	public static class BlossomTree {
		public String name;
		public BlossomTreeConfig quarkConfig;

		public BlossomLeavesBlock leaves;

		public ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey;
		public AbstractTreeGrower grower;
		public ZetaSaplingBlock sapling;
	}

	@LoadEvent
	public final void register(ZRegister event) {
		woodSet = WoodSetHandler.addWoodSet(event, this, "blossom", MapColor.COLOR_RED, MapColor.COLOR_BROWN, true);

		blossomTrees.add(make(event, "blue_blossom", MapColor.COLOR_LIGHT_BLUE, blue, BLUE_BLOSSOM_KEY));
		blossomTrees.add(make(event, "lavender_blossom", MapColor.COLOR_PINK, lavender, LAVENDER_BLOSSOM_KEY));
		blossomTrees.add(make(event, "orange_blossom", MapColor.TERRACOTTA_ORANGE, orange, ORANGE_BLOSSOM_KEY));
		blossomTrees.add(make(event, "yellow_blossom", MapColor.COLOR_YELLOW, yellow, YELLOW_BLOSSOM_KEY));
		blossomTrees.add(make(event, "red_blossom", MapColor.COLOR_RED, red, RED_BLOSSOM_KEY));
	}

	private BlossomTree make(ZRegister event, String regname, MapColor color, BlossomTreeConfig quarkConfig, ResourceKey<ConfiguredFeature<?, ?>> configuredFeatureKey) {
		BlossomTree tree = new BlossomTree();

		tree.name = regname;
		tree.quarkConfig = quarkConfig;
		tree.leaves = new BlossomLeavesBlock(regname, this, color);

		TreeConfiguration treeCfg = new TreeConfiguration.TreeConfigurationBuilder(
				BlockStateProvider.simple(woodSet.log),
				new FancyTrunkPlacer(8, 10, 10),
				BlockStateProvider.simple(tree.leaves),
				new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 4),
				new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
		).ignoreVines().build();

		// NOTE: treeFeature is intentionally not stored in a field.
		// If it was accesible from Quark, overriding it with a datapack (for the 7 people who care about doing that) won't work.
		ConfiguredFeature<TreeConfiguration, TreeFeature> treeFeature = new ConfiguredFeature<>((TreeFeature) Feature.TREE, treeCfg);
		event.getRegistry().registerDynamic(treeFeature, configuredFeatureKey, Registries.CONFIGURED_FEATURE);

		tree.configuredFeatureKey = configuredFeatureKey;
		tree.grower = new PassthruTreeGrower(configuredFeatureKey);
		tree.sapling = new ZetaSaplingBlock(regname, this, tree.grower);

		event.getVariantRegistry().addFlowerPot(tree.sapling, zeta.registry.getRegistryName(tree.sapling, BuiltInRegistries.BLOCK).getPath(), Functions.identity()); //sure

		return tree;
	}

	@LoadEvent
	public void setup(ZCommonSetup e) {
		e.enqueueWork(() -> {
			for(BlossomTree tree : blossomTrees) {
				WorldGenHandler.addGenerator(this, new BlossomTreeGenerator(tree.quarkConfig, tree.configuredFeatureKey), Decoration.TOP_LAYER_MODIFICATION, WorldGenWeights.BLOSSOM_TREES);

				ComposterBlock.COMPOSTABLES.put(tree.leaves.asItem(), 0.3F);
				ComposterBlock.COMPOSTABLES.put(tree.sapling.asItem(), 0.3F);
			}
		});
	}

	@PlayEvent
	public void addAdditionalHints(ZGatherHints event) {
		for(BlossomTree tree : blossomTrees)
			event.hintItem(tree.sapling);
	}

	private static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, Quark.asResource(name));
	}

}
