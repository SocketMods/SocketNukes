package dev.socketmods.socketnukes.capability;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SocketNukes.MODID)
public class Capabilities {

    @CapabilityInject(IConfiguration.class)
    public static Capability<IConfiguration> EXPLODER_CONFIGURATION_CAPABILITY = null;

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> e) {
        if(e.getObject().getItem() == SNRegistry.EXPLODER_ITEM.get()) {
            ConfigProvider provider = new ConfigProvider();
            e.addCapability(new ResourceLocation(SocketNukes.MODID, "config"), provider);
            e.addListener(provider::invalidate);
        }
    }
}
