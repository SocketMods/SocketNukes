package dev.socketmods.socketnukes.tileentity;

import dev.socketmods.socketnukes.recipes.CommonRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class RecipeTileEntity<T extends Recipe<?>> extends CommonTileEntity {

    protected RecipeType<?> type;

    public RecipeTileEntity(BlockEntityType<?> tileEntityTypeIn, RecipeType<?> recipeType, BlockPos blockPos, BlockState blockState) {
        super(tileEntityTypeIn, blockPos, blockState);
        type = recipeType;
    }

    @Nullable
    protected abstract T getRecipe(ItemStack stack);

    public static Set<Recipe<?>> findRecipeByType(RecipeType<?> recipeType) {
        ClientLevel world = Minecraft.getInstance().level;
        return world != null ?
            world.getRecipeManager().getRecipes().stream().filter(iRecipe -> iRecipe.getType() == recipeType).collect(Collectors.toSet())
            : Collections.emptySet();
    }

    public static Set<Recipe<?>> findRecipeByType(RecipeType<?> recipeType, @Nullable Level world) {
        return world != null ?
            world.getRecipeManager().getRecipes().stream().filter(
                iRecipe -> iRecipe.getType() == recipeType
            ).collect(Collectors.toSet())
            : Collections.emptySet();
    }

    public static Set<ItemStack> getAllRecipeInputs(RecipeType<?> type, Level world) {
        Set<ItemStack> inputs = new HashSet<>();
        Set<Recipe<?>> recipes = findRecipeByType(type, world);
        for (Recipe<?> recipe : recipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(
                ingredient ->
                    inputs.addAll(Arrays.asList(ingredient.getItems()))
            );
        }
        return inputs;
    }

    @Nullable
    public abstract T getRecipeFromOutput(ItemStack result);

    public static Set<Item> getAllRecipeOutputAsItems(RecipeType<?> type, Level world) {
        Set<Item> outputs = new HashSet<>();
        Set<Recipe<?>> recipes = findRecipeByType(type, world);
        for (Recipe<?> recipe : recipes) {
            if (recipe instanceof CommonRecipe) {
                NonNullList<ItemStack> output = ((CommonRecipe) recipe).getOutput();
                output.forEach(itemStack -> outputs.add(itemStack.getItem()));
            } else {
                ItemStack output = recipe.getResultItem();
                outputs.add(output.getItem());
            }
        }
        return outputs;
    }

    public static Set<Item> getAllRecipeInputsAsItems(RecipeType<?> type, Level world) {
        Set<Item> inputs = new HashSet<>();
        Set<Recipe<?>> recipes = findRecipeByType(type, world);
        for (Recipe<?> recipe : recipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            ingredients.forEach(ingredient -> Arrays.stream(ingredient.getItems()).collect(Collectors.toList()).forEach(itemStack -> inputs.add(itemStack.getItem())));
        }
        return inputs;
    }

    protected static void splitAndSpawnExperience(Level world, BlockPos pos, int craftedAmount, float experience) {
        int i = Mth.floor((float) craftedAmount * experience);
        float f = Mth.frac((float) craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        while (i > 0) {
            int j = ExperienceOrb.getExperienceValue(i);
            i -= j;
            world.addFreshEntity(new ExperienceOrb(world, pos.getX(), pos.getY(), pos.getZ(), j));
        }
    }
}
