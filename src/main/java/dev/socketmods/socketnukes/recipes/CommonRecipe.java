package dev.socketmods.socketnukes.recipes;

import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class CommonRecipe extends SNRecipe {

    protected final NonNullList<ItemStack> result;
    protected final NonNullList<Ingredient> ingredients;
    protected final int timer;
    protected final float xp;

    public CommonRecipe(ResourceLocation id, IRecipeType<?> recipeType, NonNullList<Ingredient> ingredients, int timer, NonNullList<ItemStack> result, float xp) {
        super(id, recipeType);
        this.ingredients = ingredients;
        this.timer = timer;
        this.result = result;
        this.xp = xp;
    }

    private static void splitAndSpawnExperience(World world, Vector3d pos, int craftedAmount, float experience) {
        int i = MathHelper.floor((float) craftedAmount * experience);
        float f = MathHelper.frac((float) craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        while (i > 0) {
            int j = ExperienceOrbEntity.getXPSplit(i);
            i -= j;
            world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
        }

    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public NonNullList<ItemStack> getOutput() {
        NonNullList<ItemStack> copy = NonNullList.create();
        copy.addAll(result);
        return copy;
    }

    @Override
    public float getXp() {
        return xp;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return result.get(0);
    }
}
