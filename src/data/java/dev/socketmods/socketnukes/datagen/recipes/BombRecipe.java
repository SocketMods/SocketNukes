package dev.socketmods.socketnukes.datagen.recipes;

import dev.socketmods.socketnukes.datagen.utils.recipies.SockRecipeProvider;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public class BombRecipe extends SockRecipeProvider {

    public BombRecipe(Consumer<IFinishedRecipe> consumer) {
        super(consumer);
    }

    @Override
    public void init() {

    }
}
