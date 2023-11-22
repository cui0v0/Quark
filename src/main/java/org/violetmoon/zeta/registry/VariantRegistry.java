package org.violetmoon.zeta.registry;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.block.ZetaSlabBlock;
import org.violetmoon.zeta.block.ZetaStairsBlock;
import org.violetmoon.zeta.block.ZetaWallBlock;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.block.IZetaBlock;

//another kinda-weird formerly singleton class
public class VariantRegistry {

	protected final Zeta zeta;

	public VariantRegistry(Zeta zeta) {
		this.zeta = zeta;
	}

	public final List<Block> slabs = new LinkedList<>();
	public final List<Block> stairs = new LinkedList<>();
	public final List<Block> walls = new LinkedList<>();

	public Block addSlabStairsWall(IZetaBlock block) {
		addSlabAndStairs(block);
		addWall(block);
		return block.getBlock();
	}

	public IZetaBlock addSlabAndStairs(IZetaBlock block) {
		addSlab(block);
		addStairs(block);
		return block;
	}

	public IZetaBlock addSlab(IZetaBlock block) {
		slabs.add(new ZetaSlabBlock(block).setCondition(block::doesConditionApply));
		return block;
	}

	public IZetaBlock addStairs(IZetaBlock block) {
		stairs.add(new ZetaStairsBlock(block).setCondition(block::doesConditionApply));
		return block;
	}

	public IZetaBlock addWall(IZetaBlock block) {
		walls.add(new ZetaWallBlock(block).setCondition(block::doesConditionApply));
		return block;
	}

	public FlowerPotBlock addFlowerPot(Block block, String name, Function<Block.Properties, Block.Properties> propertiesFunc) {
		Block.Properties props = Block.Properties.of().strength(0F).pushReaction(PushReaction.DESTROY);
		props = propertiesFunc.apply(props);

		FlowerPotBlock potted = new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, () -> block, props);
		Quark.ZETA.renderLayerRegistry.put(potted, RenderLayerRegistry.Layer.CUTOUT);
		ResourceLocation resLoc = Quark.ZETA.registry.getRegistryName(block, BuiltInRegistries.BLOCK);
		if (resLoc == null)
			resLoc = new ResourceLocation("missingno");

		Quark.ZETA.registry.registerBlock(potted, "potted_" + name, false);
		Quark.ZETA.pottedPlantRegistry.addPot(resLoc, potted);

		return potted;
	}

	// hmm (TODO: unused ever since Quark removed magma bricks)

	public static BlockBehaviour.Properties realStateCopy(IZetaBlock parent) {
		BlockBehaviour.Properties props = BlockBehaviour.Properties.copy(parent.getBlock());
		if(parent instanceof IVariantsShouldBeEmissive)
			props = props.emissiveRendering((s, r, p) -> true);

		return props;
	}

	public interface IVariantsShouldBeEmissive {}

}
