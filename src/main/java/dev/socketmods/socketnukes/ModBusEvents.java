package dev.socketmods.socketnukes;

import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SocketNukes.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusEvents {

    @SubscribeEvent
    public static void setupAttributes(EntityAttributeCreationEvent event) {
        event.put(SNRegistry.EXPLOSIVE_BOLB_TYPE.get(), BolbEntity.setupAttributes());
    }
}
