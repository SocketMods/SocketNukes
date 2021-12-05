package dev.socketmods.socketnukes.networking.packet;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * Tells the client that an explosion happens, and they need to update their blocks and entities.
 * Contains the list of blocks to be deleted, the particle and sound to display, and the entities to move.
 *
 * Packet layout:
 * int - list size
 * for every value in int:
 *  BlockPos - source
 *  BlockPos... - affected blocks
 *
 * ResourceLocation - particle
 * ResourceLocation - sound
 *
 * int - map size
 * for every value in int:
 *  int - entity ID
 *  double, double, double - vector3d
 *
 * @author Citrine
 */
public class ExtendedExplosionPacket {

    protected final List<BlockPos> affectedBlocks;
    protected final ParticleType<?> explosionParticle;
    protected final SoundEvent explosionSound;
    protected final Map<Integer, Vec3> entityDisplacements;

    // Deserialiser - construct this class from a packet, according to the above layout
    public ExtendedExplosionPacket(FriendlyByteBuf buf) {
        affectedBlocks = new ArrayList<>();
        int listSize = buf.readInt();
        for(int i = 0; i < listSize; i++) {
            affectedBlocks.add(buf.readBlockPos());
        }

        explosionParticle = ForgeRegistries.PARTICLE_TYPES.getValue(buf.readResourceLocation());
        explosionSound = ForgeRegistries.SOUND_EVENTS.getValue(buf.readResourceLocation());

        entityDisplacements = new HashMap<>();
        int mapSize = buf.readInt();
        for (int i = 0; i < mapSize; i++) {
            int entityID = buf.readInt();
            Vec3 displacement = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            entityDisplacements.put(entityID, displacement);
        }
    }

    public ExtendedExplosionPacket(BlockPos source, List<BlockPos> affectedBlocks, ParticleType<?> explosionParticle, SoundEvent explosionSound, Map<Entity, Vec3> entityDisplacements) {
        this.affectedBlocks = new ArrayList<>();
        this.affectedBlocks.add(source);
        this.affectedBlocks.addAll(affectedBlocks);

        this.explosionParticle = explosionParticle;
        this.explosionSound = explosionSound;
        this.entityDisplacements = new HashMap<>();
        for(Map.Entry<Entity, Vec3> entry : entityDisplacements.entrySet()) {
            this.entityDisplacements.put(entry.getKey().getId(), entry.getValue());
        }
    }

    // Serialiser - turn this class into a packet, according to the above layout
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.affectedBlocks.size());
        for (BlockPos pos: affectedBlocks) {
            buf.writeBlockPos(pos);
        }

        buf.writeResourceLocation(SNRegistry.getName(this.explosionParticle));
        buf.writeResourceLocation(SNRegistry.getName(this.explosionSound));

        buf.writeInt(this.entityDisplacements.size());
        for(Map.Entry<Integer, Vec3> entry : this.entityDisplacements.entrySet()) {
            buf.writeInt(entry.getKey());
            buf.writeDouble(entry.getValue().x);
            buf.writeDouble(entry.getValue().y);
            buf.writeDouble(entry.getValue().z);
        }
    }

    // Consumer - perform the intended actions once this packet is received.
    // That being: Delete the blocks, move the entities, show the particles, play the sound.
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientLevel world = Minecraft.getInstance().level;
            if (world == null) return;

            BlockPos source = affectedBlocks.get(0);
            for(BlockPos pos : affectedBlocks)  {
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }

            for(Map.Entry<Integer, Vec3> entry : this.entityDisplacements.entrySet()) {
                Entity entity = world.getEntity(entry.getKey());
                if(entity != null)
                    entity.setDeltaMovement(entry.getValue());
            }

            world.addParticle((ParticleOptions) explosionParticle, source.getX(), source.getY(), source.getZ(), 0, 0, 0);
            world.playLocalSound(source, explosionSound, SoundSource.BLOCKS, 1f, 1f, false);
        });
        return true;
    }
}
