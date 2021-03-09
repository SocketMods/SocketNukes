package dev.socketmods.socketnukes;

import dev.socketmods.socketnukes.networking.Network;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SocketNukes.MODID)
public class SocketNukes {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "socketnukes";

    public SocketNukes() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                ModelLoader.addSpecialModel(new ResourceLocation(SocketNukes.MODID, "block/hat"))
        );

        SNRegistry.initialize();
        Network.setup();

    }

}
