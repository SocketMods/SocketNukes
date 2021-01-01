package dev.socketmods.socketnukes.tileentity;

import dev.socketmods.socketnukes.recipes.CommonRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class EssentialsRecipeTileEntity<T extends IRecipe<?>> extends EssentialsCommonTileEntity {

  protected IRecipeType<?> type;

  public EssentialsRecipeTileEntity(TileEntityType<?> tileEntityTypeIn, IRecipeType<?> recipeType) {
    super(tileEntityTypeIn);
    type = recipeType;
  }

  protected abstract T getRecipe(ItemStack stack);

  public static Set<IRecipe<?>> findRecipeByType(IRecipeType<?> recipeType) {
    ClientWorld world = Minecraft.getInstance().world;
    return world != null ?
        world.getRecipeManager().getRecipes().stream().filter(iRecipe -> iRecipe.getType() == recipeType).collect(Collectors.toSet())
        : Collections.emptySet();
  }

  public static Set<IRecipe<?>> findRecipeByType(IRecipeType<?> recipeType, World world) {
    return world != null ?
        world.getRecipeManager().getRecipes().stream().filter(
            iRecipe -> iRecipe.getType() == recipeType
        ).collect(Collectors.toSet())
        : Collections.emptySet();
  }

  public static Set<ItemStack> getAllRecipeInputs(IRecipeType<?> type, World world) {
    Set<ItemStack> inputs = new HashSet<>();
    Set<IRecipe<?>> recipes = findRecipeByType(type, world);
    for (IRecipe<?> recipe : recipes) {
      NonNullList<Ingredient> ingredients = recipe.getIngredients();
      ingredients.forEach(
          ingredient ->
              inputs.addAll(Arrays.asList(ingredient.getMatchingStacks()))
      );
    }
    return inputs;
  }

  public abstract T getRecipeFromOutput(ItemStack result);

  public static Set<Item> getAllRecipeOutputAsItems(IRecipeType<?> type, World world) {
    Set<Item> outputs = new HashSet<>();
    Set<IRecipe<?>> recipes = findRecipeByType(type, world);
    for (IRecipe<?> recipe : recipes) {
      if (recipe instanceof CommonRecipe) {
        NonNullList<ItemStack> output = ((CommonRecipe) recipe).getOutput();
        output.forEach(itemStack -> outputs.add(itemStack.getItem()));
      } else {
        ItemStack output = recipe.getRecipeOutput();
        outputs.add(output.getItem());
      }
    }
    return outputs;
  }

  public static Set<Item> getAllRecipeInputsAsItems(IRecipeType<?> type, World world) {
    Set<Item> inputs = new HashSet<>();
    Set<IRecipe<?>> recipes = findRecipeByType(type, world);
    for (IRecipe<?> recipe : recipes) {
      NonNullList<Ingredient> ingredients = recipe.getIngredients();
      ingredients.forEach(ingredient -> Arrays.stream(ingredient.getMatchingStacks()).collect(Collectors.toList()).forEach(itemStack -> inputs.add(itemStack.getItem())));
    }
    return inputs;
  }

  protected static void splitAndSpawnExperience(World world, BlockPos pos, int craftedAmount, float experience) {
    int i = MathHelper.floor((float) craftedAmount * experience);
    float f = MathHelper.frac((float) craftedAmount * experience);
    if (f != 0.0F && Math.random() < (double) f) {
      ++i;
    }

    while (i > 0) {
      int j = ExperienceOrbEntity.getXPSplit(i);
      i -= j;
      world.addEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), j));
    }
  }
}
