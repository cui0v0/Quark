package vazkii.zeta.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import vazkii.zeta.recipe.IZetaConditionSerializer;
import vazkii.zeta.recipe.IZetaIngredientSerializer;

public interface CraftingExtensionsRegistry {
	// yes the generic is bad but blame forge
	IZetaConditionSerializer<?> registerConditionSerializer(IZetaConditionSerializer<?> serializer);

	<T extends Ingredient> IZetaIngredientSerializer<T> registerIngredientSerializer(ResourceLocation id, IZetaIngredientSerializer<T> serializer);
	ResourceLocation getID(IZetaIngredientSerializer<?> serializer);

	void addBrewingRecipe(Potion input, Ingredient reagent, Potion output);
}
