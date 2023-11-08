package org.violetmoon.zeta.client.event.load;

import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public interface ZPreTextureStitch extends IZetaLoadEvent {
	TextureAtlas getAtlas();
	boolean addSprite(ResourceLocation sprite);
}
