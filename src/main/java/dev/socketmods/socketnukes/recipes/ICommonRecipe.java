package dev.socketmods.socketnukes.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface ICommonRecipe extends IRecipe<RecipeWrapper> {

    @Override
    default boolean canCraftInDimensions(int width, int height) {
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
    ItemStack getResultItem();

    @Deprecated
    @Override
    ItemStack assemble(RecipeWrapper inv);
}
