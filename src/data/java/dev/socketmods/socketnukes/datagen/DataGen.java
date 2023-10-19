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
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
        var output = event.getGenerator().getPackOutput();
        generator.addProvider(event.includeServer(), new RecipeProviders(output));
        generator.addProvider(event.includeServer(), new AdvancementsProvider(event));
        generator.addProvider(event.includeServer(), new GLMProvider(output));
        generator.addProvider(event.includeServer(), new LootTableProviders(output));
        BlockTagProviders blockTags = new BlockTagProviders(event);
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ItemTagProviders(event, blockTags));
        generator.addProvider(event.includeServer(), new FluidTagsProviders(event));

        LOGGER.info(DATAGEN, "Adding data providers for client assets");
        BlockModelProviders models = new BlockModelProviders(output, event.getExistingFileHelper());
        generator.addProvider(event.includeClient(), models);
        generator.addProvider(event.includeClient(), new ItemModelProviders(output, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new EnUsLangProvider(output));
        generator.addProvider(event.includeClient(), new BlockStateProvider(output, event.getExistingFileHelper(), models));

    }

}
