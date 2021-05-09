package dev.socketmods.socketnukes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.socketmods.socketnukes.client.ClientThingDoer;
import dev.socketmods.socketnukes.networking.Network;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(SocketNukes.MODID)
public class SocketNukes {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "socketnukes";

    public SocketNukes() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientThingDoer::registerSpecialModels);

        SNRegistry.initialize();
        Network.setup();
    }

}
