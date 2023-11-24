package org.violetmoon.quark.content.building.client.render.be;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.util.HashMap;
import java.util.Map;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.client.render.GenericChestBERenderer;
import org.violetmoon.quark.content.building.module.VariantChestsModule.IChestTextureProvider;

public class VariantChestRenderer extends GenericChestBERenderer<ChestBlockEntity> {

	private static final Map<Block, ChestTextureBatch> chestTextures = new HashMap<>();

	public VariantChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Material getMaterial(ChestBlockEntity t, ChestType type) {
		Block block = t.getBlockState().getBlock();

		ChestTextureBatch batch = chestTextures.get(block);
		if(batch == null)
			return null;

		return switch (type) {
			case LEFT -> batch.left;
			case RIGHT -> batch.right;
			default -> batch.normal;
		};
	}


	private static class ChestTextureBatch {
		public final Material normal, left, right;

		public ChestTextureBatch(ResourceLocation atlas, ResourceLocation normal, ResourceLocation left, ResourceLocation right) {
			this.normal = new Material(atlas, normal);
			this.left = new Material(atlas, left);
			this.right = new Material(atlas, right);
		}

	}

}
