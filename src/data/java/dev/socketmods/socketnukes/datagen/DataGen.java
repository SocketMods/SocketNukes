package dev.socketmods.socketnukes.datagen;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.datagen.advancment.AdvancementsProvider;
import dev.socketmods.socketnukes.datagen.blockmodel.BlockModelProviders;
import dev.socketmods.socketnukes.datagen.global_loot_modifier.GLMProvider;
import dev.socketmods.socketnukes.datagen.itemmodel.ItemModelProviders;
import dev.socketmods.socketnukes.datagen.lang.EnUsLangProvider;
import dev.socketmods.socketnukes.datagen.loot_tables.LootTableProviders;
import dev.socketmods.socketnukes.datagen.recipes.RecipeProviders;
import dev.socketmods.socketnukes.datagen.tags.BlockTagProviders;
import dev.socketmods.socketnukes.datagen.tags.FluidTagsProviders;
import dev.socketmods.socketnukes.datagen.tags.ItemTagProviders;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import static dev.socketmods.socketnukes.SocketNukes.LOGGER;

@Mod.EventBusSubscriber(modid = SocketNukes.MODID)
public class DataGen {
    public static final Marker DATAGEN = MarkerManager.getMarker("DATAGEN");

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        LOGGER.info(DATAGEN, "Gathering data providers");
        DataGenerator generator = event.getGenerator();
        if(event.includeServer()){
            LOGGER.info(DATAGEN, "Adding data providers for server data");
            generator.addProvider(new RecipeProviders(generator));
            generator.addProvider(new AdvancementsProvider(generator));
            generator.addProvider(new GLMProvider(generator));
            generator.addProvider(new LootTableProviders(generator));
            generator.addProvider(new RecipeProviders(generator));
            BlockTagsProvider blockTags = new BlockTagProviders(generator, event.getExistingFileHelper());
            generator.addProvider(blockTags);
            generator.addProvider(new ItemTagProviders(generator, blockTags, event.getExistingFileHelper()));
            generator.addProvider(new FluidTagsProviders(generator, event.getExistingFileHelper()));
        }

        if(event.includeClient()){
            LOGGER.info(DATAGEN, "Adding data providers for client assets");
            generator.addProvider(new BlockModelProviders(generator, event.getExistingFileHelper()));
            generator.addProvider(new ItemModelProviders(generator, event.getExistingFileHelper()));
            generator.addProvider(new EnUsLangProvider(generator));
        }
    }
}
