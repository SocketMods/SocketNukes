package dev.socketmods.socketnukes.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
        material.add("ingredient", ingredient.toJson());
        material.addProperty("count", count);
        return material;
    }

    public static Material deserialize(JsonObject object) {
        Ingredient ingredient = CraftingHelper.getIngredient(object.get("ingredient"));
        int count = GsonHelper.getAsInt(object, "count", 1);
        if (count <= 0) {
            throw new IllegalArgumentException("Material count must be a positive integer.");
        }
        return new Material(ingredient, count);
    }

    public void write(FriendlyByteBuf packet) {
        packet.writeVarInt(count);
        ingredient.toNetwork(packet);
    }

    public static Material read(FriendlyByteBuf packet) {
        int count = packet.readVarInt();
        Ingredient ingredient = Ingredient.fromNetwork(packet);
        return new Material(ingredient, count);
    }
}
