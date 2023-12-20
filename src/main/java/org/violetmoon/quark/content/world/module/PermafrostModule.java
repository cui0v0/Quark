package org.violetmoon.quark.content.world.module;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.MapColor;

import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.base.config.type.CompoundBiomeConfig;
import org.violetmoon.quark.base.world.WorldGenHandler;
import org.violetmoon.quark.base.world.WorldGenWeights;
import org.violetmoon.quark.content.world.undergroundstyle.PermafrostStyle;
import org.violetmoon.quark.content.world.undergroundstyle.base.UndergroundStyleConfig;
import org.violetmoon.quark.content.world.undergroundstyle.base.UndergroundStyleGenerator;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.OldMaterials;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "world")
public class PermafrostModule extends ZetaModule {

	@Config
	public UndergroundStyleConfig<PermafrostStyle> generationSettings = new UndergroundStyleConfig<>(new PermafrostStyle(), 2, 100, 30, 10, 5,
		CompoundBiomeConfig.fromBiomeReslocs(false, "minecraft:frozen_peaks"));
	{
		generationSettings.minYLevel = 105;
		generationSettings.maxYLevel = 140;
	}

	@Hint
	public static ZetaBlock permafrost;

	@LoadEvent
	public final void register(ZRegister event) {
		CreativeTabManager.daisyChain();
		permafrost = (ZetaBlock) new ZetaBlock("permafrost", this,
				OldMaterials.stone()
						.mapColor(MapColor.COLOR_LIGHT_BLUE)
						.requiresCorrectToolForDrops()
						.strength(1.5F, 10F)
						.sound(SoundType.STONE))
				.setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, Blocks.DEEPSLATE, true);

		event.getVariantRegistry().addSlabStairsWall(permafrost, null);
		event.getVariantRegistry().addSlabStairsWall((IZetaBlock) new ZetaBlock("permafrost_bricks", this, Block.Properties.copy(permafrost)).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS), null);
		CreativeTabManager.endDaisyChain();

		generationSettings.biomeObj.setBlock(permafrost.defaultBlockState());
	}

	@LoadEvent
	public final void setup(ZCommonSetup event) {
		WorldGenHandler.addGenerator(this,
			new UndergroundStyleGenerator<>(generationSettings, "permafrost"),
			GenerationStep.Decoration.UNDERGROUND_DECORATION,
			WorldGenWeights.UNDERGROUND_BIOMES
		);
	}

}
