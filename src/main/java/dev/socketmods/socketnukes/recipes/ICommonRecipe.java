package dev.socketmods.socketnukes.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.core.NonNullList;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface ICommonRecipe extends Recipe<RecipeWrapper> {

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
