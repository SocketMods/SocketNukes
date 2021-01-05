package dev.socketmods.socketnukes.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface ICommonRecipe extends IRecipe<RecipeWrapper> {

    @Override
    default boolean canFit(int width, int height) {
        return false;
    }

    int getTimer();

    NonNullList<ItemStack> getOutput();

    float getXp();

    /**
     * Use the NonNullList Version {@link ICommonRecipe#getOutput()}
     */
    @Deprecated
    @Override
    ItemStack getRecipeOutput();

    @Deprecated
    @Override
    ItemStack getCraftingResult(RecipeWrapper inv);
}
