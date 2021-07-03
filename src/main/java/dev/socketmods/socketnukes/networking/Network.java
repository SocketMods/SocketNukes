package dev.socketmods.socketnukes.networking;

import dev.socketmods.socketnukes.networking.packet.ExploderConfigChangedPacket;
import dev.socketmods.socketnukes.networking.packet.ExtendedExplosionPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static dev.socketmods.socketnukes.SocketNukes.MODID;

/**
 * Central network handling class.
 * All of the packets sent for the mod should go through one of the channels here.
 * All of the packets sent by the mod should go through one of the helper methods here.
 */
public class Network {
    private static final String networkVer = "1";

    private static int PACKETID = 0;

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "explosions"),
            () -> networkVer,
            networkVer::equals,
            networkVer::equals
    );

    // Register valid packets, called from SocketNukes, the main class
    public static void setup() {
        CHANNEL.messageBuilder(ExtendedExplosionPacket.class, PACKETID++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ExtendedExplosionPacket::toBytes)
                .decoder(ExtendedExplosionPacket::new)
                .consumer(ExtendedExplosionPacket::handle)
                .add();
        CHANNEL.messageBuilder(ExploderConfigChangedPacket.class, PACKETID++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(ExploderConfigChangedPacket::toBytes)
                .decoder(ExploderConfigChangedPacket::new)
                .consumer(ExploderConfigChangedPacket::handle)
                .add();
    }

    // Send an arbitrary packet to the given player, from the server.
    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        CHANNEL.sendTo(packet, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }

    // Send an arbitrary packet to the server, from the client.
    public static void sendToServer(Object packet) {
        CHANNEL.sendToServer(packet);
    }
}
