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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod(SocketNukes.MODID)
public class SocketNukes {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "socketnukes";

    // These people, developers and contributors of the mod, will be given a bolbmas hat in-game.
    public static final List<String> BOLBERS = new ArrayList(Arrays.asList(
            "d23dfef7-36a5-40aa-b851-6b8201e0c779", // mthwzrd / sciwhiz12
            "7133ccb3-efc0-47c9-b3f1-44b04464f06c", // Curle
            "4cf999cf-0773-4624-bfcb-d014c13fdf34", // Unbekannt
            "86a71cfb-8a07-4f35-9560-5ee28d183264", // dpeter99
            "d64548d3-d532-42fa-b0bb-cfc96b0dd72a"  // AterAnimAvis)
    ));

    public SocketNukes() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                ModelLoader.addSpecialModel(new ResourceLocation(SocketNukes.MODID, "block/hat"))
        );

        SNRegistry.initialize();
        Network.setup();

    }

}
