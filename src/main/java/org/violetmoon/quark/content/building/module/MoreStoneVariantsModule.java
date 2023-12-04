package org.violetmoon.quark.content.building.module;

import java.util.function.BooleanSupplier;

import org.violetmoon.quark.base.config.Config;
import org.violetmoon.quark.base.config.ConfigFlagManager;
import org.violetmoon.quark.content.building.block.MyalitePillarBlock;
import org.violetmoon.quark.content.world.block.MyaliteBlock;
import org.violetmoon.quark.content.world.module.NewStoneTypesModule;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.block.ZetaBlockWrapper;
import org.violetmoon.zeta.block.ZetaPillarBlock;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.loading.ZGatherAdditionalFlags;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

@ZetaLoadModule(category = "building")
public class MoreStoneVariantsModule extends ZetaModule {

	@Config(flag = "stone_bricks") public boolean enableBricks = true;
	@Config(flag = "stone_chiseled") public boolean enableChiseledBricks = true;
	@Config(flag = "stone_pillar") public boolean enablePillar = true;
	
	@LoadEvent
	public final void register(ZRegister event) {
		Block polishedCalcite = expandVanillaStone(event, this, Blocks.CALCITE, "calcite");
		Block polishedDripstone = expandVanillaStone(event, this, Blocks.DRIPSTONE_BLOCK, "dripstone");
		Block polishedTuff = expandVanillaStone(event, this, Blocks.TUFF, "tuff");
		
		BooleanSupplier _true = () -> true;
		add(event, "granite", MapColor.DIRT, SoundType.STONE, Blocks.POLISHED_GRANITE, _true);
		add(event, "diorite", MapColor.QUARTZ, SoundType.STONE,Blocks.POLISHED_DIORITE, _true);
		add(event, "andesite", MapColor.STONE, SoundType.STONE, Blocks.POLISHED_ANDESITE, _true);
		add(event, "calcite", MapColor.TERRACOTTA_WHITE, SoundType.CALCITE, polishedCalcite, _true);
		add(event, "dripstone", MapColor.TERRACOTTA_BROWN, SoundType.DRIPSTONE_BLOCK, polishedDripstone, _true);
		add(event, "tuff", MapColor.TERRACOTTA_GRAY, SoundType.TUFF, polishedTuff, _true);
		
		add(event, "limestone", MapColor.STONE, SoundType.STONE, NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.limestoneBlock), () -> NewStoneTypesModule.enableLimestone);
		add(event, "jasper", MapColor.TERRACOTTA_RED, SoundType.STONE, NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.jasperBlock),() -> NewStoneTypesModule.enableJasper);
		add(event, "shale", MapColor.ICE, SoundType.STONE, NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.shaleBlock), () -> NewStoneTypesModule.enableShale);
		
		add(event, "myalite", MapColor.COLOR_PURPLE, SoundType.STONE, NewStoneTypesModule.polishedBlocks.get(NewStoneTypesModule.myaliteBlock), () -> NewStoneTypesModule.enableMyalite, MyaliteBlock::new, MyalitePillarBlock::new);
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
	
	public Block expandVanillaStone(ZRegister event, ZetaModule module, Block raw, String name) {
		ZetaBlockWrapper wrap = new ZetaBlockWrapper(raw, this);
		CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.BUILDING_BLOCKS, wrap, Blocks.DEEPSLATE, true);
		
		return NewStoneTypesModule.makeStone(event, module, raw, name, null, null, () -> true, null, ZetaBlock::new);
	}
	
	private void add(ZRegister event, String name, MapColor color, SoundType sound, Block basePolished, BooleanSupplier cond) {
		add(event, name, color, sound, basePolished, cond, ZetaBlock::new, ZetaPillarBlock::new);
	}
	
	private void add(ZRegister event, String name, MapColor color, SoundType sound, Block basePolished, BooleanSupplier cond, ZetaBlock.Constructor<ZetaBlock> constr, ZetaBlock.Constructor<ZetaPillarBlock> pillarConstr) {
		Block.Properties props = Block.Properties.of()
				.requiresCorrectToolForDrops()
				.instrument(NoteBlockInstrument.BASEDRUM)
				.mapColor(color)
				.sound(sound)
				.strength(1.5F, 6.0F);
		
		CreativeTabManager.daisyChain();
		ZetaBlock bricks = (ZetaBlock) constr.make(name + "_bricks", this,  props)
				.setCondition(() -> cond.getAsBoolean() && enableBricks)
				.setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS, basePolished, false);
		constr.make("chiseled_" + name + "_bricks", this, props)
				.setCondition(() -> cond.getAsBoolean() && enableBricks && enableChiseledBricks)
				.setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
		pillarConstr.make(name + "_pillar", this, props)
				.setCondition(() -> cond.getAsBoolean() && enablePillar)
				.setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
		
		event.getVariantRegistry().addSlabStairsWall(bricks, null);
		CreativeTabManager.endDaisyChain();
	}
	
}
