package dev.socketmods.socketnukes.datagen.utils.recipes;

import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public abstract class SocketRecipeProvider implements ISocketRecipeProvider {

  protected Consumer<IFinishedRecipe> consumer;

  public SocketRecipeProvider(Consumer<IFinishedRecipe> consumer) {
    this.consumer = consumer;
    init();
  }
}
