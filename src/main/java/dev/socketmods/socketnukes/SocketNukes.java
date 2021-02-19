package dev.socketmods.socketnukes;

import dev.socketmods.socketnukes.networking.Network;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SocketNukes.MODID)
public class SocketNukes {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "socketnukes";

    public SocketNukes() {
        SNRegistry.initialize();
        Network.setup();
    }
}
