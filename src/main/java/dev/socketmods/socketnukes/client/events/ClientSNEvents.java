package dev.socketmods.socketnukes.client.events;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.client.render.ExplosiveEntityRenderer;
import dev.socketmods.socketnukes.client.render.bolb.BolbEntityRenderer;
import dev.socketmods.socketnukes.client.render.layer.PlayerHatLayer;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Runs all the client-only events. Currently, only Entity Rendering.
 *
 * @author Citrine
 */
@Mod.EventBusSubscriber(modid = SocketNukes.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSNEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // Register the Entity Renderers
        RenderingRegistry.registerEntityRenderingHandler(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), ExplosiveEntityRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(SNRegistry.BOLB_ENTITY_TYPE.get(), BolbEntityRenderer::new);

        // Add the Player Hat Layer for the two skin types.
        // We need to do this for both default and slim
        event.enqueueWork(() -> {
            EntityRendererManager manager = Minecraft.getInstance().getRenderManager();

            addPlayerHatLayer(manager, "default");
            addPlayerHatLayer(manager, "slim");
        });
    }

    private static void addPlayerHatLayer(EntityRendererManager manager, String skin) {
        PlayerRenderer playerRenderer = manager.getSkinMap().get(skin);
        playerRenderer.addLayer(new PlayerHatLayer(playerRenderer));
    }

    @SubscribeEvent
    public static void bakeModels(ModelBakeEvent event) {
        PlayerHatLayer.HAT_MODEL = event.getModelRegistry().get(new ResourceLocation(SocketNukes.MODID, "block/hat"));
    }
}
