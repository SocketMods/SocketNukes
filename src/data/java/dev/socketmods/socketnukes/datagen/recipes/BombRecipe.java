package dev.socketmods.socketnukes.datagen.recipes;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.datagen.builder.ShapedRecipeWithNBTBuilder;
import dev.socketmods.socketnukes.datagen.utils.recipes.SocketRecipeProvider;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BombRecipe extends SocketRecipeProvider {

    public BombRecipe(Consumer<IFinishedRecipe> consumer) {
        super(consumer);
    }

    @Override
    public void init() {
        createRecipe(SNRegistry.NULL_EXPLOSION, Items.TNT, Items.BLUE_DYE, Items.AIR);
        createRecipe(SNRegistry.BOLB_EXPLOSION, Items.TNT, Items.SLIME_BALL, Items.RED_DYE);
        createRecipe(SNRegistry.CUBIC_EXPLOSION, Items.TNT, Items.QUARTZ_PILLAR, Items.QUARTZ_PILLAR);
        createRecipe(SNRegistry.VANILLA_EXPLOSION, Items.TNT, Items.SAND, Items.GUNPOWDER);
    }

    /**
     * create the NBT Values for the Recipe
     * @param type the Explosion Type that should be set
     * @return the NBT Value that is set for the recipe
     */
    public static CompoundNBT forExplosion(ExtendedExplosionType type) {
        CompoundNBT data = new CompoundNBT();
        data.putString("explosion", SNRegistry.getName(type).toString());

        CompoundNBT nbt = new CompoundNBT();
        nbt.put(SocketNukes.MODID, data);

        return nbt;
    }

    /**
     * generates a Recipe for an Explosion Type
     * @param type The Explosion Type the Recipe should be created for
     * @param mid The Item in the middle of the grid
     * @param midOuter The items adjacent to the center
     * @param corners the 4 corners if this is {@link Items#AIR} the corners are empty
     */
    private void createRecipe(Supplier<? extends ExtendedExplosionType> type, IItemProvider mid,
                              IItemProvider midOuter, IItemProvider corners){
        ShapedRecipeWithNBTBuilder recipe = ShapedRecipeWithNBTBuilder.shapedRecipe(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get());
        if(corners != Items.AIR){
            recipe.patternLine("RSR")
                    .patternLine("STS")
                    .patternLine("RSR")
                    .key('R', corners)
                    .key('S', midOuter)
                    .key('T', mid);
        }else {
            recipe.patternLine(" S ")
                    .patternLine("STS")
                    .patternLine(" S ")
                    .key('S', midOuter)
                    .key('T', mid);
        }

        recipe.addCriterion("has_tnt", RecipeProviders.hasItem(Items.TNT))
                .setNBT(forExplosion(type.get()))
                .build(consumer, SNRegistry.getName(type) +"_explosive");
    }
}
