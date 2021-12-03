package dev.socketmods.socketnukes.recipes;

import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class CommonRecipe extends SNRecipe {

    protected final NonNullList<ItemStack> result;
    protected final NonNullList<Ingredient> ingredients;
    protected final int timer;
    protected final float xp;

    public CommonRecipe(ResourceLocation id, RecipeType<?> recipeType, NonNullList<Ingredient> ingredients, int timer, NonNullList<ItemStack> result, float xp) {
        super(id, recipeType);
        this.ingredients = ingredients;
        this.timer = timer;
        this.result = result;
        this.xp = xp;
    }

    private static void splitAndSpawnExperience(Level world, Vec3 pos, int craftedAmount, float experience) {
        int i = Mth.floor((float) craftedAmount * experience);
        float f = Mth.frac((float) craftedAmount * experience);
        if (f != 0.0F && Math.random() < (double) f) {
            ++i;
        }

        while (i > 0) {
            int j = ExperienceOrb.getExperienceValue(i);
            i -= j;
            world.addFreshEntity(new ExperienceOrb(world, pos.x, pos.y, pos.z, j));
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
    public ItemStack assemble(RecipeWrapper inv) {
        return result.get(0);
    }
}
