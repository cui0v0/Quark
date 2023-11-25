package org.violetmoon.quark.content.building.client.render.be;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.client.render.GenericChestBERenderer;
import org.violetmoon.quark.content.building.module.VariantChestsModule;

public class VariantChestRenderer extends GenericChestBERenderer<ChestBlockEntity> {

	public VariantChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Material getMaterial(ChestBlockEntity tile, ChestType type) {
		if(!(tile.getBlockState().getBlock() instanceof VariantChestsModule.IChestTextureProvider prov))
			return null;

		StringBuilder tex = new StringBuilder(prov.getChestTexturePath());

		//apply the texture naming convention
		if(prov.isTrap())
			tex.append(choose(type, "trap", "trap_left", "trap_right"));
		else
			tex.append(choose(type, "normal", "left", "right"));

		return new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Quark.MOD_ID, tex.toString()));
	}

}
