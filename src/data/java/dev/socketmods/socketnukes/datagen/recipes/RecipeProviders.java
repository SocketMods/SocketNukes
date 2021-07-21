package dev.socketmods.socketnukes.datagen.recipes;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

import java.util.function.Consumer;

public class RecipeProviders extends RecipeProvider {

    public RecipeProviders(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        new BombRecipe(consumer);

    }

    // expose the protected internal methods so we can use them in external classes
    public static ICriterionInstance hasItem(Item item) {
        return RecipeProvider.has(item);
    }

    public static ICriterionInstance hasItemTag(ITag<Item> tagItem) {
        return RecipeProvider.has(tagItem);
    }
}
