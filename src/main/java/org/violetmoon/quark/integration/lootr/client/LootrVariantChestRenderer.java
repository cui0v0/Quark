package org.violetmoon.quark.integration.lootr.client;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.ChestType;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.util.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.client.render.GenericChestBERenderer;
import org.violetmoon.quark.content.building.module.VariantChestsModule;
import org.violetmoon.quark.integration.lootr.LootrVariantChestBlockEntity;

public class LootrVariantChestRenderer<T extends LootrVariantChestBlockEntity> extends GenericChestBERenderer<T> {
	private UUID playerId = null;
	private static final Map<Block, ChestTextureBatch> chestTextures = new HashMap<>();

	public LootrVariantChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Material getMaterial(T tile, ChestType type) {
		Block block = tile.getBlockState().getBlock();

		ChestTextureBatch batch = chestTextures.get(block);
		if (batch == null)
			return null;

		if (ConfigManager.isVanillaTextures()) {
			return batch.base;
		}

		if (playerId == null) {
			Player player = Getter.getPlayer();
			if (player != null) {
				playerId = player.getUUID();
			} else {
				return batch.unopened;
			}
		}
		if (tile.isOpened()) {
			return batch.opened;
		}
		if (tile.getOpeners().contains(playerId)) {
			return batch.opened;
		} else {
			return batch.unopened;
		}
	}


	private static class ChestTextureBatch {
		public final Material base, unopened, opened;

		public ChestTextureBatch(ResourceLocation atlas, ResourceLocation base, ResourceLocation unopened, ResourceLocation opened) {
			this.base = new Material(atlas, base);
			this.unopened = new Material(atlas, unopened);
			this.opened = new Material(atlas, opened);
		}

	}
}
