package dev.socketmods.socketnukes.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface ICommonRecipe extends Recipe<RecipeWrapper> {

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    int getTimer();

    NonNullList<ItemStack> getOutput(RegistryAccess p_267052_);

    float getXp();

    /**
     * Use the NonNullList Version {@link ICommonRecipe#getOutput(RegistryAccess)}
     */
    @Deprecated
    @Override
    ItemStack getResultItem(RegistryAccess p_267052_);

    @Deprecated
    @Override
    ItemStack assemble(RecipeWrapper inv, RegistryAccess p_267165_);
}
