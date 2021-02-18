package dev.socketmods.socketnukes.client.events;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.client.render.ExplosiveEntityRenderer;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SocketNukes.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSNEvents {

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {

        EntityRendererManager manager = Minecraft.getInstance().getRenderManager();
        manager.register(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), new ExplosiveEntityRenderer(manager));
    }
}
