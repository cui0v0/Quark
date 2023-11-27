package org.violetmoon.quark.content.world.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.module.BlossomTreesModule;
import org.violetmoon.zeta.block.ZetaSaplingBlock;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.OptionalInt;

public class BlossomSaplingBlock extends ZetaSaplingBlock {
	static String colorName;

	public BlossomSaplingBlock(String colorName, ZetaModule module, BlossomTree tree) {
		super(colorName + "_blossom", module, tree);
		tree.sapling = this;
		BlossomSaplingBlock.colorName = colorName;
	}

	public static class BlossomTree extends AbstractTreeGrower {

		public final TreeConfiguration config;
		public final BlockState leaf;
		public BlossomSaplingBlock sapling;

		public BlossomTree(Block leafBlock) {
			config = (new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(BlossomTreesModule.woodSet.log),
					new FancyTrunkPlacer(8, 10, 10),
					BlockStateProvider.simple(leafBlock),
					new FancyFoliagePlacer(ConstantInt.of(3), ConstantInt.of(1), 4),
					new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))))
					.ignoreVines()
					.build();

			leaf = leafBlock.defaultBlockState();
		}

		public static final ResourceKey<ConfiguredFeature<?, ?>> BLUE_BLOSSOM_KEY = registerKey("blue_blossom");
		public static final ResourceKey<ConfiguredFeature<?, ?>> LAVENDER_BLOSSOM_KEY = registerKey("lavender_blossom");
		public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_BLOSSOM_KEY = registerKey("orange_blossom");
		public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_BLOSSOM_KEY = registerKey("yellow_blossom");
		public static final ResourceKey<ConfiguredFeature<?, ?>> RED_BLOSSOM_KEY = registerKey("red_blossom");

		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource rand, boolean hasFlowers) {
			ResourceKey<ConfiguredFeature<?, ?>> blossomType;

			switch (colorName) {
				case "blue" -> blossomType = BLUE_BLOSSOM_KEY;
				case "lavender" -> blossomType = LAVENDER_BLOSSOM_KEY;
				case "orange" -> blossomType = ORANGE_BLOSSOM_KEY;
				case "yellow" -> blossomType = YELLOW_BLOSSOM_KEY;
				case "red" -> blossomType = RED_BLOSSOM_KEY;
				default -> {
					return null;
				}

			}

			return blossomType;
		}

		//fixme find how to call this
//		public void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
//			context.register(BLOSSOM_TREE, new ConfiguredFeature<>(Feature.TREE, config));
//		}

		public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
			return ResourceKey.create(Registries.CONFIGURED_FEATURE, Quark.asResource(name));
		}
	}

}
