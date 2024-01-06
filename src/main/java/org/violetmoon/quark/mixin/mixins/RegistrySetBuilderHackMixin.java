package org.violetmoon.quark.mixin.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.content.world.module.GlimmeringWealdModule;

import java.util.List;

// Some mods call VanillaRegistries.createLookup which for some reason throw some exceptionwith modded biomes.
// This is a terrible temporary fix until the real issue is found
@Mixin(RegistrySetBuilder.BuildState.class)
public class RegistrySetBuilderHackMixin {

    @Shadow @Final private List<RuntimeException> errors;

    @WrapOperation(method = "reportRemainingUnreferencedValues", at = @At(value = "INVOKE",
    target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    public <E>boolean quark$preventInvalidGWBiome(List instance, E e, Operation<Boolean> original, @Local ResourceKey<Object> resourcekey){
        if(resourcekey.location().equals(GlimmeringWealdModule.BIOME_NAME)){
            return false;
        }
        return errors.add((RuntimeException) e);
    }
}
