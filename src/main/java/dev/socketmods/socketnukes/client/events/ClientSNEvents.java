package dev.socketmods.socketnukes.client.events;

import java.util.Objects;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.client.render.ExplosiveEntityRenderer;
import dev.socketmods.socketnukes.client.render.SNModelLayers;
import dev.socketmods.socketnukes.client.render.bolb.BolbEntityRenderer;
import dev.socketmods.socketnukes.client.render.bolb.BolbHatLayer;
import dev.socketmods.socketnukes.client.render.bolb.BolbModel;
import dev.socketmods.socketnukes.client.render.layer.PlayerHatLayer;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Runs all the client-only events. Currently, only Entity Rendering.
 *
 * @author Citrine
 */
@Mod.EventBusSubscriber(modid = SocketNukes.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSNEvents {

    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), ExplosiveEntityRenderer::new);
        event.registerEntityRenderer(SNRegistry.BOLB_ENTITY_TYPE.get(), BolbEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SNModelLayers.BOLB, BolbModel::createInnerBodyLayer);
        event.registerLayerDefinition(SNModelLayers.BOLB_OUTER, BolbModel::createOuterBodyLayer);
        event.registerLayerDefinition(SNModelLayers.BOLB_HAT, BolbHatLayer::createLayer);
    }

    @SubscribeEvent
    public static void entityLayers(EntityRenderersEvent.AddLayers event) {
        event.getSkins().forEach(skin -> addPlayerHatLayer(Objects.requireNonNull(event.getSkin(skin))));
    }

    /**
     * I hate generics.
     * EntityRenderersEvent$AddLayers#getSkin returns an erased type R extends LivingEntityRenderer.
     * Using the raw type causes pain and suffering on an unimaginable scale.
     * Forcing it through this function as a parameter with defined type solves suffering and ends world hunger.
     * I hate generics.
     */
    private static void addPlayerHatLayer(LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> renderer) {
        renderer.addLayer(new PlayerHatLayer(renderer));
    }

    @SubscribeEvent
    public static void bakeModels(ModelBakeEvent event) {
        PlayerHatLayer.HAT_MODEL = event.getModelRegistry().get(new ResourceLocation(SocketNukes.MODID, "block/hat"));
    }
}
