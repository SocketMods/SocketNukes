package dev.socketmods.socketnukes.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.function.Predicate;

public class Material implements Predicate<ItemStack> {
  public final Ingredient ingredient;
  public final int count;

  private Material(Ingredient ingredient, int count) {
    this.ingredient = ingredient;
    this.count = count;
  }

  public static Material of(Ingredient ingredient, int count) {
    return new Material(ingredient, count);
  }

  @Override
  public boolean test(ItemStack itemStack) {
    return ingredient.test(itemStack) && itemStack.getCount() >= count;
  }

  public JsonObject serialize() {
    JsonObject material = new JsonObject();
    material.add("ingredient", ingredient.serialize());
    material.addProperty("count", count);
    return material;
  }

  public static Material deserialize(JsonObject object) {
    Ingredient ingredient = CraftingHelper.getIngredient(object.get("ingredient"));
    int count = JSONUtils.getInt(object, "count", 1);
    if (count <= 0) {
      throw new IllegalArgumentException("Material count must be a positive integer.");
    }
    return new Material(ingredient, count);
  }

  public void write(PacketBuffer packet) {
    packet.writeVarInt(count);
    ingredient.write(packet);
  }

  public static Material read(PacketBuffer packet) {
    int count = packet.readVarInt();
    Ingredient ingredient = Ingredient.read(packet);
    return new Material(ingredient, count);
  }
}
