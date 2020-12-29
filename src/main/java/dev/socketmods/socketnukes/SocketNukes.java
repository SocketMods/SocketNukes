package dev.socketmods.socketnukes;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SocketNukes.MODID)
public class SocketNukes {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "socketnukes";

    public SocketNukes() {

        SNRegistry.initialize();

    }
}
