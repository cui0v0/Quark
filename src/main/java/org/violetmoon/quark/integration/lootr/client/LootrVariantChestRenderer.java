package org.violetmoon.quark.integration.lootr.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.ChestType;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.util.Getter;

import java.util.UUID;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.client.render.GenericChestBERenderer;
import org.violetmoon.quark.content.building.module.VariantChestsModule;
import org.violetmoon.quark.integration.lootr.LootrVariantChestBlockEntity;

public class LootrVariantChestRenderer<T extends LootrVariantChestBlockEntity> extends GenericChestBERenderer<T> {
	private UUID playerId = null;

	public LootrVariantChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Material getMaterial(T tile, ChestType type) {
		if(!(tile.getBlockState().getBlock() instanceof VariantChestsModule.IChestTextureProvider prov))
			return null;

		//lazy-init pattern
		if(playerId == null) {
			Player player = Getter.getPlayer();
			if(player != null)
				playerId = player.getUUID();
		}

		boolean opened = tile.isOpened() || tile.getOpeners().contains(playerId);

		StringBuilder tex = new StringBuilder(prov.getChestTexturePath());

		//apply the texture naming convention
		if(prov.isTrap()) {
			if(ConfigManager.isVanillaTextures())
				tex.append("trap");
			else if(opened)
				tex.append("lootr_trap_opened");
			else
				tex.append("lootr_trap");
		} else {
			if(ConfigManager.isVanillaTextures())
				tex.append("normal");
			else if(opened)
				tex.append("lootr_opened");
			else
				tex.append("lootr_normal");
		}

		return new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Quark.MOD_ID, tex.toString()));
	}
}
