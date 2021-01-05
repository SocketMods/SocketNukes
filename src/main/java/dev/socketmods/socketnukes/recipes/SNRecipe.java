package dev.socketmods.socketnukes.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public abstract class SNRecipe implements ICommonRecipe {
    protected ResourceLocation id;
    protected IRecipeType<?> recipeType;

    public SNRecipe(ResourceLocation id, IRecipeType<?> recipeType) {
        this.id = id;
        this.recipeType = recipeType;
    }

    @Override
    @Nonnull
    public IRecipeType<?> getType() {
        return recipeType;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public abstract NonNullList<Ingredient> getIngredients();

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    public boolean hasAdditionalInput() {
        return getIngredients().size() > 1;
    }
}
