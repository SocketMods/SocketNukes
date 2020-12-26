package dev.socketmods.socketnukes.datagen;

import dev.socketmods.socketnukes.SocketNukes;
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
    }
}
