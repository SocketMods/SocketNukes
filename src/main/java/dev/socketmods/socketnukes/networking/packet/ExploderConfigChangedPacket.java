package dev.socketmods.socketnukes.networking.packet;

import dev.socketmods.socketnukes.capability.Capabilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ExploderConfigChangedPacket {

    private int playerID;
    private String config;

    public ExploderConfigChangedPacket(PacketBuffer buf) {
        config = buf.readString();
        playerID = buf.readInt();
    }

    public ExploderConfigChangedPacket(String config, int player) {
        this.config = config;
        this.playerID = player;
    }


    public void toBytes(PacketBuffer buf) {
        buf.writeString(config);
        buf.writeInt(playerID);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ctx.get().getSender().getHeldItemMainhand().getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap ->
                    cap.setConfig(config));
        });

        return true;
    }


}
