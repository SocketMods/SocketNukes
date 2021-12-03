package dev.socketmods.socketnukes.capability;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.capability.exploderconfig.ConfigurationCap;
import dev.socketmods.socketnukes.capability.exploderconfig.IConfiguration;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * A central location to store and attach capabilities.
 *
 * @author Citrine
 */
@Mod.EventBusSubscriber(modid = SocketNukes.MODID)
public class Capabilities {

    public static final Capability<ConfigurationCap> EXPLODER_CONFIGURATION_CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>(){});

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<ItemStack> e) {
        if(e.getObject().getItem() == SNRegistry.EXPLODER_ITEM.get()) {
            ConfigurationCap provider = new ConfigurationCap();
            e.addCapability(new ResourceLocation(SocketNukes.MODID, "config"), provider);
            e.addListener(provider::invalidate);
        }
    }
}
