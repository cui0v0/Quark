package vazkii.quark.content.building.module;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import vazkii.quark.base.Quark;
import vazkii.zeta.client.AlikeColorHandler;
import vazkii.zeta.client.event.ZAddBlockColorHandlers;
import vazkii.zeta.client.event.ZAddItemColorHandlers;
import vazkii.zeta.module.ZetaLoadModule;
import vazkii.zeta.module.ZetaModule;
import vazkii.quark.base.util.VanillaWoods;
import vazkii.quark.base.util.VanillaWoods.Wood;
import vazkii.quark.content.building.block.HedgeBlock;
import vazkii.quark.content.world.block.BlossomSaplingBlock.BlossomTree;
import vazkii.quark.content.world.module.AncientWoodModule;
import vazkii.quark.content.world.module.BlossomTreesModule;
import vazkii.zeta.event.ZCommonSetup;
import vazkii.zeta.event.ZRegister;
import vazkii.zeta.event.bus.LoadEvent;

@ZetaLoadModule(category = "building")
public class HedgesModule extends ZetaModule {

	public static TagKey<Block> hedgesTag;
	
	@LoadEvent
	public final void register(ZRegister event) {
		for(Wood wood : VanillaWoods.OVERWORLD)
			new HedgeBlock(this, wood.fence(), wood.leaf());
		
		new HedgeBlock(this, Blocks.OAK_FENCE, Blocks.AZALEA_LEAVES);
		new HedgeBlock(this, Blocks.OAK_FENCE, Blocks.FLOWERING_AZALEA_LEAVES);
	}

	@LoadEvent
	public void postRegister(ZRegister.Post e) {
		for (BlossomTree tree : BlossomTreesModule.trees.keySet())
			new HedgeBlock(this, BlossomTreesModule.woodSet.fence, tree.leaf.getBlock()).setCondition(tree.sapling::isEnabled);
		
		new HedgeBlock(this, AncientWoodModule.woodSet.fence, AncientWoodModule.ancient_leaves).setCondition(() -> Quark.ZETA.modules.isEnabled(AncientWoodModule.class));
	}
	
	@LoadEvent
	public final void setup(ZCommonSetup event) {
		hedgesTag = BlockTags.create(new ResourceLocation(Quark.MOD_ID, "hedges"));
	}

	@ZetaLoadModule(clientReplacement = true)
	public static class Client extends HedgesModule {

		@LoadEvent
		public void blockColorProviders(ZAddBlockColorHandlers event) {
			event.registerNamed(b -> new AlikeColorHandler((HedgeBlock) b, HedgeBlock::getLeaf), "hedge");
		}

		@LoadEvent
		public void itemColorProviders(ZAddItemColorHandlers event) {
			event.registerNamed(i -> new AlikeColorHandler(i, HedgeBlock::getLeaf), "hedge");
		}

	}

}
