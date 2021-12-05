package dev.socketmods.socketnukes.datagen.recipes;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.datagen.builder.ShapedRecipeWithNBTBuilder;
import dev.socketmods.socketnukes.datagen.utils.recipes.SocketRecipeProvider;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BombRecipe extends SocketRecipeProvider {

    public BombRecipe(Consumer<FinishedRecipe> consumer) {
        super(consumer);
    }

    /**
     * create the NBT Values for the Recipe
     *
     * @param type the Explosion Type that should be set
     * @return the NBT Value that is set for the recipe
     */
    public static CompoundTag forExplosion(ExtendedExplosionType type) {
        CompoundTag data = new CompoundTag();
        data.putString("explosion", SNRegistry.getName(type).toString());

        CompoundTag nbt = new CompoundTag();
        nbt.put(SocketNukes.MODID, data);

        return nbt;
    }

    @Override
    public void init() {
        createRecipe(SNRegistry.NULL_EXPLOSION, Items.TNT, Items.BLUE_DYE, Items.AIR);
        createRecipe(SNRegistry.BOLB_EXPLOSION, Items.TNT, Items.SLIME_BALL, Items.RED_DYE);
        createRecipe(SNRegistry.CUBIC_EXPLOSION, Items.TNT, Items.QUARTZ_PILLAR, Items.QUARTZ_PILLAR);
        createRecipe(SNRegistry.VANILLA_EXPLOSION, Items.TNT, Items.SAND, Items.GUNPOWDER);
    }

    /**
     * generates a Recipe for an Explosion Type
     *
     * @param type     The Explosion Type the Recipe should be created for
     * @param mid      The Item in the middle of the grid
     * @param midOuter The items adjacent to the center
     * @param corners  the 4 corners if this is {@link Items#AIR} the corners are empty
     */
    private void createRecipe(Supplier<? extends ExtendedExplosionType> type, ItemLike mid,
                              ItemLike midOuter, ItemLike corners) {
        ShapedRecipeWithNBTBuilder.shapedRecipe(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get())
                .patternLine("RSR")
                .patternLine("STS")
                .patternLine("RSR")
                .key('R', corners)
                .key('S', midOuter)
                .key('T', mid)
                .addCriterion("has_tnt", RecipeProviders.hasItem(Items.TNT))
                .setNBT(forExplosion(type.get()))
                .build(consumer, SNRegistry.getName(type) + "_explosive");
    }
}
