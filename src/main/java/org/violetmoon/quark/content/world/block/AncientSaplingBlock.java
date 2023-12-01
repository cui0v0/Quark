package org.violetmoon.quark.content.world.block;

import com.google.common.collect.Lists;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.feature.AncientTreeTopperDecorator;
import org.violetmoon.quark.content.world.feature.MultiFoliageStraightTrunkPlacer;
import org.violetmoon.quark.content.world.module.AncientWoodModule;
import org.violetmoon.zeta.block.ZetaSaplingBlock;
import org.violetmoon.zeta.module.ZetaModule;

import org.jetbrains.annotations.NotNull;
import java.util.OptionalInt;

public class AncientSaplingBlock extends ZetaSaplingBlock {

	public AncientSaplingBlock(ZetaModule module) {
		super("ancient", module, new AncientTree());
	}

	public static class AncientTree extends AbstractTreeGrower {

		public final TreeConfiguration config;

		public AncientTree() {
			config = (new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(AncientWoodModule.woodSet.log),
					new MultiFoliageStraightTrunkPlacer(17, 4, 6, 5, 3),
					BlockStateProvider.simple(AncientWoodModule.ancient_leaves),
					new FancyFoliagePlacer(UniformInt.of(2, 4), ConstantInt.of(-3) , 2),
					new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))))
					.decorators(Lists.newArrayList(new AncientTreeTopperDecorator()))
					.ignoreVines()
					.build();
		}

		ResourceKey<ConfiguredFeature<?, ?>> ANCIENT_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, Quark.asResource("ancient_tree"));

		@Override
		protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource rand, boolean hasFlowers) {
			return ANCIENT_TREE;
		}

		//fixme find how to call this
		public void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
			context.register(ANCIENT_TREE, new ConfiguredFeature<>(Feature.TREE, config));
		}
	}

}
