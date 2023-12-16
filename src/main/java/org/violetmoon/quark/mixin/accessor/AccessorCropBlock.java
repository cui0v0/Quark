package org.violetmoon.quark.mixin.accessor;

import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CropBlock.class)
public interface AccessorCropBlock {
	@Accessor("AGE")
	IntegerProperty quark$getAgeProperty();
}
