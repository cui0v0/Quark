package org.violetmoon.quark.content.building.client.render.be;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.building.module.VariantChestsModule;

import java.util.IdentityHashMap;
import java.util.Map;

public class VariantChestRenderer extends ChestRenderer<ChestBlockEntity> {

    private final Map<Block, Material> materialMap = new IdentityHashMap<>();
    protected final boolean isTrap;

    public VariantChestRenderer(BlockEntityRendererProvider.Context context, boolean isTrap) {
        super(context);
        this.isTrap = isTrap;
    }

    @Nullable
    @Override
    public Material getMaterial(ChestBlockEntity tile, ChestType type) {
        Block block = tile.getBlockState().getBlock();

        return materialMap.computeIfAbsent(block, b -> {
            if (!(block instanceof VariantChestsModule.IVariantChest v)) return null;
            //apply the texture naming convention
            StringBuilder tex = new StringBuilder(v.getTextureFolder())
					.append('/')
                    .append(v.getTexturePath())
                    .append('/');
            if (isTrap)
                tex.append(choose(type, "trap", "trap_left", "trap_right"));
            else
                tex.append(choose(type, "normal", "left", "right"));

            return new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Quark.MOD_ID, tex.toString()));
        });
    }

    protected <X> X choose(ChestType type, X single, X left, X right) {
        return switch (type) {
            case SINGLE -> single;
            case LEFT -> left;
            case RIGHT -> right;
        };
    }

}
