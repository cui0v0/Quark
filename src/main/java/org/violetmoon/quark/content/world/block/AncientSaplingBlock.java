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

	public AncientSaplingBlock(ZetaModule module, AbstractTreeGrower grower) {
		super("ancient", module, grower);
	}

}
