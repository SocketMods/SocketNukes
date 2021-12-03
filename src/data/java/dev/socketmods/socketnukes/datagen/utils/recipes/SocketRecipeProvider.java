package dev.socketmods.socketnukes.datagen.utils.recipes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

public abstract class SocketRecipeProvider implements ISocketRecipeProvider {

  protected Consumer<FinishedRecipe> consumer;

  public SocketRecipeProvider(Consumer<FinishedRecipe> consumer) {
    this.consumer = consumer;
    init();
  }
}
