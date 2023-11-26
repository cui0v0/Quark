package org.violetmoon.quark.integration.lootr;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.integration.lootr.client.LootrVariantChestRenderer;
import org.violetmoon.zeta.client.SimpleWithoutLevelRenderer;
import org.violetmoon.zeta.module.ZetaModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * @author WireSegal
 * Created at 11:40 AM on 7/3/23.
 */
public class LootrIntegration implements ILootrIntegration {

	private BlockEntityType<LootrVariantChestBlockEntity> chestTEType;
	private BlockEntityType<LootrVariantTrappedChestBlockEntity> trappedChestTEType;

	private final Map<Block, Block> chestMappings = new HashMap<>();

	private final List<Block> lootrRegularChests = new ArrayList<>();
	private final List<Block> lootrTrappedChests = new ArrayList<>();

	@Override
	public BlockEntityType<? extends ChestBlockEntity> chestTE() {
		return chestTEType;
	}

	@Override
	public BlockEntityType<? extends ChestBlockEntity> trappedChestTE() {
		return trappedChestTEType;
	}

	@Override
	public void makeChestBlocks(ZetaModule module, String name, Block base, BooleanSupplier condition, Block quarkRegularChest, Block quarkTrappedChest) {
		Block regularLootrChest = new LootrVariantChestBlock(name, module, () -> chestTEType, BlockBehaviour.Properties.copy(base));
		lootrRegularChests.add(regularLootrChest);

		Block trappedLootrChest = new LootrVariantTrappedChestBlock(name, module, () -> trappedChestTEType, BlockBehaviour.Properties.copy(base));
		lootrTrappedChests.add(trappedLootrChest);

		chestMappings.put(quarkRegularChest, regularLootrChest);
		chestMappings.put(quarkTrappedChest, trappedLootrChest);
	}

	@Override
	@Nullable
	public Block lootrVariant(Block base) {
		return chestMappings.get(base);
	}

	@Override
	public void postRegister() {
		chestTEType = BlockEntityType.Builder.of(LootrVariantChestBlockEntity::new, lootrRegularChests.toArray(new Block[0])).build(null);
		trappedChestTEType = BlockEntityType.Builder.of(LootrVariantTrappedChestBlockEntity::new, lootrTrappedChests.toArray(new Block[0])).build(null);

		Quark.ZETA.registry.register(chestTEType, "lootr_variant_chest", Registries.BLOCK_ENTITY_TYPE);
		Quark.ZETA.registry.register(trappedChestTEType, "lootr_variant_trapped_chest", Registries.BLOCK_ENTITY_TYPE);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		BlockEntityRenderers.register(chestTEType, ctx -> new LootrVariantChestRenderer<>(ctx, false));
		BlockEntityRenderers.register(trappedChestTEType, ctx -> new LootrVariantChestRenderer<>(ctx, true));

		for(Block b : lootrRegularChests)
			QuarkClient.ZETA_CLIENT.setBlockEntityWithoutLevelRenderer(b.asItem(), new SimpleWithoutLevelRenderer(chestTEType, b.defaultBlockState()));
		for(Block b : lootrTrappedChests)
			QuarkClient.ZETA_CLIENT.setBlockEntityWithoutLevelRenderer(b.asItem(), new SimpleWithoutLevelRenderer(trappedChestTEType, b.defaultBlockState()));
	}
}
