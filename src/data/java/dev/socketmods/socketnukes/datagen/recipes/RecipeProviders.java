package dev.socketmods.socketnukes.datagen.recipes;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class RecipeProviders extends RecipeProvider {

    public RecipeProviders(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        new BombRecipe(consumer);
    }

    // expose the protected internal methods so we can use them in external classes
    public static CriterionTriggerInstance hasItem(Item item) {
        return RecipeProvider.has(item);
    }

    public static CriterionTriggerInstance hasItemTag(TagKey<Item> tagItem) {
        return RecipeProvider.has(tagItem);
    }
}
