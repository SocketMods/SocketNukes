package dev.socketmods.socketnukes.datagen.recipes;

import dev.socketmods.socketnukes.datagen.utils.recipes.SocketRecipeProvider;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public class BombRecipe extends SocketRecipeProvider {

    public BombRecipe(Consumer<IFinishedRecipe> consumer) {
        super(consumer);
    }

    @Override
    public void init() {

    }
}
