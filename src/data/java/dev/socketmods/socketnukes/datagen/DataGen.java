package dev.socketmods.socketnukes.datagen;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.datagen.advancment.AdvancementsProvider;
import dev.socketmods.socketnukes.datagen.block.model.BlockModelProviders;
import dev.socketmods.socketnukes.datagen.block.state.BlockStateProvider;
import dev.socketmods.socketnukes.datagen.global_loot_modifier.GLMProvider;
import dev.socketmods.socketnukes.datagen.itemmodel.ItemModelProviders;
import dev.socketmods.socketnukes.datagen.lang.EnUsLangProvider;
import dev.socketmods.socketnukes.datagen.loot_tables.LootTableProviders;
import dev.socketmods.socketnukes.datagen.recipes.RecipeProviders;
import dev.socketmods.socketnukes.datagen.tags.BlockTagProviders;
import dev.socketmods.socketnukes.datagen.tags.FluidTagsProviders;
import dev.socketmods.socketnukes.datagen.tags.ItemTagProviders;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import static dev.socketmods.socketnukes.SocketNukes.LOGGER;

@Mod.EventBusSubscriber(modid = SocketNukes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    public static final Marker DATAGEN = MarkerManager.getMarker("DATAGEN");

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        LOGGER.info(DATAGEN, "Gathering data providers");
        DataGenerator generator = event.getGenerator();
        LOGGER.info(DATAGEN, "Adding data providers for server data");
        generator.addProvider(event.includeServer(), new RecipeProviders(generator));
        generator.addProvider(event.includeServer(), new AdvancementsProvider(generator));
        generator.addProvider(event.includeServer(), new GLMProvider(generator));
        generator.addProvider(event.includeServer(), new LootTableProviders(generator));
        generator.addProvider(event.includeServer(), new RecipeProviders(generator));
        BlockTagsProvider blockTags = new BlockTagProviders(generator, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagProviders(generator, blockTags, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new FluidTagsProviders(generator, event.getExistingFileHelper()));

        LOGGER.info(DATAGEN, "Adding data providers for client assets");
        BlockModelProviders models = new BlockModelProviders(generator, event.getExistingFileHelper());
        generator.addProvider(event.includeClient(), models);
        generator.addProvider(event.includeClient(), new ItemModelProviders(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new EnUsLangProvider(generator));
        generator.addProvider(event.includeClient(), new BlockStateProvider(generator, event.getExistingFileHelper(), models));

    }

}
