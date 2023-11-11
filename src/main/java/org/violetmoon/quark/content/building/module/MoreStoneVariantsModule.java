package org.violetmoon.quark.content.building.module;

import java.util.function.BooleanSupplier;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.base.config.ConfigFlagManager;
import org.violetmoon.quark.content.building.block.MyalitePillarBlock;
import org.violetmoon.quark.content.world.block.MyaliteBlock;
import org.violetmoon.quark.content.world.module.NewStoneTypesModule;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.block.ZetaPillarBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.loading.ZGatherAdditionalFlags;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "building")
public class MoreStoneVariantsModule extends ZetaModule {

	@Config(flag = "stone_bricks") public boolean enableBricks = true;
	@Config(flag = "stone_chiseled") public boolean enableChiseledBricks = true;
	@Config(flag = "stone_pillar") public boolean enablePillar = true;
	
	@LoadEvent
	public final void register(ZRegister event) {
		expandVanillaStone(event, this, Blocks.CALCITE, "calcite");
		expandVanillaStone(event, this, Blocks.DRIPSTONE_BLOCK, "dripstone");
		expandVanillaStone(event, this, Blocks.TUFF, "tuff");
		
		BooleanSupplier _true = () -> true;
		add(event, "granite", MaterialColor.DIRT, SoundType.STONE, _true);
		add(event, "diorite", MaterialColor.QUARTZ, SoundType.STONE, _true);
		add(event, "andesite", MaterialColor.STONE, SoundType.STONE, _true);
		add(event, "calcite", MaterialColor.TERRACOTTA_WHITE, SoundType.CALCITE, _true);
		add(event, "dripstone", MaterialColor.TERRACOTTA_BROWN, SoundType.DRIPSTONE_BLOCK, _true);
		add(event, "tuff", MaterialColor.TERRACOTTA_GRAY, SoundType.TUFF, _true);
		
		add(event, "limestone", MaterialColor.STONE, SoundType.STONE, () -> NewStoneTypesModule.enableLimestone);
		add(event, "jasper", MaterialColor.TERRACOTTA_RED, SoundType.STONE, () -> NewStoneTypesModule.enableJasper);
		add(event, "shale", MaterialColor.ICE, SoundType.STONE, () -> NewStoneTypesModule.enableShale);
		
		add(event, "myalite", MaterialColor.COLOR_PURPLE, SoundType.STONE, () -> NewStoneTypesModule.enableMyalite, MyaliteBlock::new, MyalitePillarBlock::new);
	}

	@PlayEvent
	public final void moreFlags(ZGatherAdditionalFlags event) {
		ConfigFlagManager manager = event.flagManager();
		manager.putFlag(this, "granite", true);
		manager.putFlag(this, "diorite", true);
		manager.putFlag(this, "andesite", true);
		manager.putFlag(this, "calcite", true);
		manager.putFlag(this, "dripstone", true);
		manager.putFlag(this, "tuff", true);
	}
	
	public static void expandVanillaStone(ZRegister event, ZetaModule module, Block raw, String name) {
		NewStoneTypesModule.makeStone(event, module, raw, name, null, null, () -> true, null, ZetaBlock::new);
	}
	
	private void add(ZRegister event, String name, MaterialColor color, SoundType sound, BooleanSupplier cond) {
		add(event, name, color, sound, cond, ZetaBlock::new, ZetaPillarBlock::new);
	}
	
	private void add(ZRegister event, String name, MaterialColor color, SoundType sound, BooleanSupplier cond, ZetaBlock.Constructor<ZetaBlock> constr, ZetaBlock.Constructor<ZetaPillarBlock> pillarConstr) {
		Block.Properties props = Block.Properties.of(Material.STONE, color)
				.requiresCorrectToolForDrops()
				.sound(sound)
				.strength(1.5F, 6.0F);
		
		ZetaBlock bricks = constr.make(name + "_bricks", this, CreativeModeTab.TAB_BUILDING_BLOCKS, props)
				.setCondition(() -> cond.getAsBoolean() && enableBricks);
		event.getVariantRegistry().addSlabStairsWall(bricks);
		
		constr.make("chiseled_" + name + "_bricks", this, CreativeModeTab.TAB_BUILDING_BLOCKS, props)
				.setCondition(() -> cond.getAsBoolean() && enableBricks && enableChiseledBricks);
		pillarConstr.make(name + "_pillar", this, CreativeModeTab.TAB_BUILDING_BLOCKS, props)
				.setCondition(() -> cond.getAsBoolean() && enablePillar);
	}
	
}
