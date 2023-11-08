package org.violetmoon.quark.content.tools.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import org.violetmoon.quark.content.tools.entity.SkullPike;

public class SkullPikeRenderer extends EntityRenderer<SkullPike> {

	public SkullPikeRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(@Nonnull SkullPike entity, float yaw, float partialTicks, @Nonnull PoseStack matrix, @Nonnull MultiBufferSource buffer, int light) {
	}

	@Nonnull
	@Override
	public ResourceLocation getTextureLocation(@Nonnull SkullPike arg0) {
		return TextureAtlas.LOCATION_BLOCKS;
	}

}
