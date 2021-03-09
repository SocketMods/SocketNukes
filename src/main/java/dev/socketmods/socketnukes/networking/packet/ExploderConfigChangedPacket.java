package dev.socketmods.socketnukes.networking.packet;

import dev.socketmods.socketnukes.capability.Capabilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Tells the server that the configuration on a player's Exploder Item has changed.
 * Without this, the server will continue to execute the default "null" explosion, despite the client displaying the
 * particles of the configured explosion.
 *
 * Packet Layout:
 *  ResourceLocation - the name of the explosion type
 *  int - the ID of the player that configured their item
 *
 * @author Citrine
 */
public class ExploderConfigChangedPacket {
    private final ResourceLocation config;

    // Deserialiser - write this class into a buffer
    public ExploderConfigChangedPacket(PacketBuffer buf) {
        config = buf.readResourceLocation();
    }

    public ExploderConfigChangedPacket(ResourceLocation config) {
        this.config = config;
    }

    // Serializer - read this class out of a buffer
    public void toBytes(PacketBuffer buf) {
        buf.writeResourceLocation(config);
    }

    // Consumer - actually perform the intended task.
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ctx.get().getSender().getHeldItemMainhand().getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap ->
                    cap.setConfig(config));
        });
        return true;
    }
}
