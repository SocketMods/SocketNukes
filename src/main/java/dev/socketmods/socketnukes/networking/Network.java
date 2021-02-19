package dev.socketmods.socketnukes.networking;

import dev.socketmods.socketnukes.networking.packet.ExtendedExplosionPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static dev.socketmods.socketnukes.SocketNukes.MODID;

public class Network {
    private static final String networkVer = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "explosions"),
            () -> networkVer,
            networkVer::equals,
            networkVer::equals
    );

    public static void setup() {
        CHANNEL.messageBuilder(ExtendedExplosionPacket.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ExtendedExplosionPacket::toBytes)
                .decoder(ExtendedExplosionPacket::new)
                .consumer(ExtendedExplosionPacket::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        CHANNEL.sendTo(packet, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
