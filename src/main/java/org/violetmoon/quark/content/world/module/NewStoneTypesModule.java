package org.violetmoon.quark.content.world.module;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.base.config.type.DimensionConfig;
import org.violetmoon.quark.base.world.WorldGenHandler;
import org.violetmoon.quark.base.world.WorldGenWeights;
import org.violetmoon.quark.base.world.generator.OreGenerator;
import org.violetmoon.quark.content.world.block.MyaliteBlock;
import org.violetmoon.quark.content.world.block.MyaliteColorLogic;
import org.violetmoon.quark.content.world.config.BigStoneClusterConfig;
import org.violetmoon.quark.content.world.config.StoneTypeConfig;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.block.ZetaBlockWrapper;
import org.violetmoon.zeta.client.event.load.ZAddBlockColorHandlers;
import org.violetmoon.zeta.client.event.load.ZAddItemColorHandlers;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.function.BooleanSupplier;

@ZetaLoadModule(category = "world")
public class NewStoneTypesModule extends ZetaModule {

	@Config(flag = "limestone") public static boolean enableLimestone = true;
	@Config(flag = "jasper") public static boolean enableJasper = true;
	@Config(flag = "shale") public static boolean enableShale = true;
	@Config(flag = "myalite") public static boolean enableMyalite = true;

	public static boolean enabledWithLimestone, enabledWithJasper, enabledWithShale, enabledWithMyalite;

	@Config public static StoneTypeConfig limestone = new StoneTypeConfig();
	@Config public static StoneTypeConfig jasper = new StoneTypeConfig();
	@Config public static StoneTypeConfig shale = new StoneTypeConfig();
	@Config public static StoneTypeConfig myalite = new StoneTypeConfig(DimensionConfig.end(false));

	@Hint("limestone") public static Block limestoneBlock;
	@Hint("jasper") public static Block jasperBlock;
	@Hint("shale") public static Block shaleBlock;
	@Hint("myalite") public static Block myaliteBlock;

	public static Map<Block, Block> polishedBlocks = Maps.newHashMap();

	private static Queue<Runnable> defers = new ArrayDeque<>();

	@LoadEvent
	public final void register(ZRegister event) {
		limestoneBlock = makeStone(event, this, "limestone", limestone, BigStoneClustersModule.limestone, () -> enableLimestone, MapColor.STONE);
		jasperBlock = makeStone(event, this, "jasper", jasper, BigStoneClustersModule.jasper, () -> enableJasper, MapColor.TERRACOTTA_RED);
		shaleBlock = makeStone(event, this, "shale", shale, BigStoneClustersModule.shale, () -> enableShale, MapColor.ICE);
		myaliteBlock = makeStone(event, this, null, "myalite", myalite, BigStoneClustersModule.myalite, () -> enableMyalite, MapColor.COLOR_PURPLE, MyaliteBlock::new);
	}

	public static Block makeStone(ZRegister event, ZetaModule module, String name, StoneTypeConfig config, BigStoneClusterConfig bigConfig, BooleanSupplier enabledCond, MapColor color) {
		return makeStone(event, module, null, name, config, bigConfig, enabledCond, color, ZetaBlock::new);
	}

	public static Block makeStone(ZRegister event, ZetaModule module, final Block raw, String name, StoneTypeConfig config, BigStoneClusterConfig bigConfig, BooleanSupplier enabledCond, MapColor color, ZetaBlock.Constructor<ZetaBlock> constr) {
		BooleanSupplier trueEnabledCond = () -> (bigConfig == null || !bigConfig.enabled || !Quark.ZETA.modules.isEnabled(BigStoneClustersModule.class)) && enabledCond.getAsBoolean();

		Block.Properties props;
		if(raw != null)
			props = Block.Properties.copy(raw);
		else
			props = Block.Properties.of(Material.STONE, color)
				.requiresCorrectToolForDrops()
				.strength(1.5F, 6.0F);

		Block normal;
		if(raw != null)
			normal = raw;
		else
			normal = constr.make(name, module, CreativeModeTab.TAB_BUILDING_BLOCKS, props).setCondition(enabledCond);

		ZetaBlock polished = constr.make("polished_" + name, module, CreativeModeTab.TAB_BUILDING_BLOCKS, props).setCondition(enabledCond);
		polishedBlocks.put(normal, polished);

		event.getVariantRegistry().addSlabStairsWall(normal instanceof IZetaBlock quarkBlock ? quarkBlock : new ZetaBlockWrapper(normal, module).setCondition(enabledCond));
		event.getVariantRegistry().addSlabAndStairs(polished);

		if(raw == null) {
			defers.add(() -> {
				WorldGenHandler.addGenerator(module, new OreGenerator(config.dimensions, config.oregenLower, normal.defaultBlockState(), OreGenerator.ALL_DIMS_STONE_MATCHER, trueEnabledCond), Decoration.UNDERGROUND_ORES, WorldGenWeights.NEW_STONES);
				WorldGenHandler.addGenerator(module, new OreGenerator(config.dimensions, config.oregenUpper, normal.defaultBlockState(), OreGenerator.ALL_DIMS_STONE_MATCHER, trueEnabledCond), Decoration.UNDERGROUND_ORES, WorldGenWeights.NEW_STONES);
			});
		}

		return normal;
	}

	@LoadEvent
	public final void configChanged(ZConfigChanged event) {
		enabledWithLimestone = enableLimestone && this.enabled;
		enabledWithJasper = enableJasper && this.enabled;
		enabledWithShale = enableShale && this.enabled;
		enabledWithMyalite = enableMyalite && this.enabled;
	}

	@LoadEvent
	public final void setup(ZCommonSetup event) {
		while(!defers.isEmpty())
			defers.poll().run();
	}

	@ZetaLoadModule(clientReplacement = true)
	public static class Client extends NewStoneTypesModule {

		@LoadEvent
		public void blockColorProviders(ZAddBlockColorHandlers event) {
			event.registerNamed(block -> MyaliteColorHandler.INSTANCE, "myalite");
		}

		@LoadEvent
		public void itemColorProviders(ZAddItemColorHandlers event) {
			event.registerNamed(item -> MyaliteColorHandler.INSTANCE, "myalite");
		}

		private static class MyaliteColorHandler implements BlockColor, ItemColor {

			static final MyaliteColorHandler INSTANCE = new MyaliteColorHandler();

			@Override
			public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
				return MyaliteColorLogic.getColor(pos);
			}

			@Override
			public int getColor(ItemStack stack, int tintIndex) {
				Minecraft mc = Minecraft.getInstance();
				if(mc.player == null)
					return MyaliteColorLogic.getColor(BlockPos.ZERO);

				BlockPos pos = mc.player.blockPosition();
				HitResult res = mc.hitResult;
				if(res != null && res.getType() == HitResult.Type.BLOCK)
					pos = ((BlockHitResult) res).getBlockPos();

				return MyaliteColorLogic.getColor(pos);
			}

		}

	}

}
