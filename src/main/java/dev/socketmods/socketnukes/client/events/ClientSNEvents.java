package dev.socketmods.socketnukes.client.events;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.client.render.bolb.BolbEntityRenderer;
import dev.socketmods.socketnukes.client.render.ExplosiveEntityRenderer;
import dev.socketmods.socketnukes.client.render.layer.PlayerHatLayer;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Runs all the client-only events.
 * Currently, only Entity Rendering.
 *
 * @author Citrine
 */
@Mod.EventBusSubscriber(modid = SocketNukes.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSNEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {

        EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
        manager.register(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), new ExplosiveEntityRenderer(manager));
        manager.register(SNRegistry.EXPLOSIVE_BOLB_TYPE.get(), new BolbEntityRenderer(manager));

        manager.getSkinMap().get("default").addLayer(new PlayerHatLayer(Minecraft.getInstance().getRenderManager().playerRenderer));
        manager.getSkinMap().get("slim").addLayer(new PlayerHatLayer(Minecraft.getInstance().getRenderManager().playerRenderer));
    }

    @SubscribeEvent
    public static void bakeModels(ModelBakeEvent e) {
        PlayerHatLayer.HAT_MODEL = e.getModelRegistry().get(new ResourceLocation(SocketNukes.MODID, "block/hat"));
    }
}
