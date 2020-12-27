package dev.socketmods.socketnukes.datagen.utils.recipies;

import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public abstract class SockRecipeProvider implements ISockRecipeProvider {

  protected Consumer<IFinishedRecipe> consumer;

  public SockRecipeProvider(Consumer<IFinishedRecipe> consumer) {
    this.consumer = consumer;
    init();
  }
}
