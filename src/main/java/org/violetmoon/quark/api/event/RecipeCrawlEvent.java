package org.violetmoon.quark.api.event;

import com.google.common.collect.Multimap;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.eventbus.api.Event;
import org.violetmoon.quark.base.util.registryaccess.RegistryAccessUtil;

import java.util.Collection;

@Deprecated(forRemoval = true)
public abstract class RecipeCrawlEvent extends Event {

	public static class Reset extends RecipeCrawlEvent {}

	public static abstract class Visit<T extends Recipe<?>> extends RecipeCrawlEvent {
		
		public final T recipe;
		public final ResourceLocation recipeID;
		public final ItemStack output;
		public final NonNullList<Ingredient> ingredients;
		
		public Visit(T recipe) {
			this.recipe = recipe;
			this.recipeID = recipe.getId();
			this.output = recipe.getResultItem(RegistryAccessUtil.getRegistryAccess());
			this.ingredients = recipe.getIngredients();
		}

		public static class Shapeless extends Visit<ShapelessRecipe> {

			public Shapeless(ShapelessRecipe recipe) {
				super(recipe);
			}
			
		}
		
		public static class Custom extends Visit<CustomRecipe> {

			public Custom(CustomRecipe recipe) {
				super(recipe);
			}
			
		}
		
		public static class Cooking extends Visit<AbstractCookingRecipe> {

			public Cooking(AbstractCookingRecipe recipe) {
				super(recipe);
			}
			
		}
		
		public static class Misc extends Visit<Recipe<?>> {

			public Misc(Recipe<?> recipe) {
				super(recipe);
			}
			
		}
		
	}
	
}

