package dev.socketmods.socketnukes.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;

public abstract class SNRecipe implements ICommonRecipe {
    protected ResourceLocation id;
    protected RecipeType<?> recipeType;

    public SNRecipe(ResourceLocation id, RecipeType<?> recipeType) {
        this.id = id;
        this.recipeType = recipeType;
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return recipeType;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public abstract NonNullList<Ingredient> getIngredients();

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return ItemStack.EMPTY;
    }

    public boolean hasAdditionalInput() {
        return getIngredients().size() > 1;
    }
}
