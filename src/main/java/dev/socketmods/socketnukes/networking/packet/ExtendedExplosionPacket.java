package dev.socketmods.socketnukes.networking.packet;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;
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
    protected final Map<Integer, Vector3d> entityDisplacements;

    // Deserialiser - construct this class from a packet, according to the above layout
    public ExtendedExplosionPacket(PacketBuffer buf) {
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
            Vector3d displacement = new Vector3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
            entityDisplacements.put(entityID, displacement);
        }
    }

    public ExtendedExplosionPacket(BlockPos source, List<BlockPos> affectedBlocks, ParticleType<?> explosionParticle, SoundEvent explosionSound, Map<Entity, Vector3d> entityDisplacements) {
        this.affectedBlocks = new ArrayList<>();
        this.affectedBlocks.add(source);
        this.affectedBlocks.addAll(affectedBlocks);

        this.explosionParticle = explosionParticle;
        this.explosionSound = explosionSound;
        this.entityDisplacements = new HashMap<>();
        for(Map.Entry<Entity, Vector3d> entry : entityDisplacements.entrySet()) {
            this.entityDisplacements.put(entry.getKey().getEntityId(), entry.getValue());
        }
    }

    // Serialiser - turn this class into a packet, according to the above layout
    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.affectedBlocks.size());
        for (BlockPos pos: affectedBlocks) {
            buf.writeBlockPos(pos);
        }

        buf.writeResourceLocation(this.explosionParticle.getRegistryName());
        buf.writeResourceLocation(this.explosionSound.getRegistryName());

        buf.writeInt(this.entityDisplacements.size());
        for(Map.Entry<Integer, Vector3d> entry : this.entityDisplacements.entrySet()) {
            buf.writeInt(entry.getKey());
            buf.writeDouble(entry.getValue().x);
            buf.writeDouble(entry.getValue().y);
            buf.writeDouble(entry.getValue().z);
        }
    }

    // Consumer - perform the intended actions once this packet is recieved.
    // That being: Delete the blocks, move the entities, show the particles, play the sound.
    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            BlockPos source = affectedBlocks.get(0);
            for(BlockPos pos : affectedBlocks)  {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
            for(Map.Entry<Integer, Vector3d> entry : this.entityDisplacements.entrySet()) {
                world.getEntityByID(entry.getKey()).setMotion(entry.getValue());
            }

            world.addParticle((IParticleData) explosionParticle, source.getX(), source.getY(), source.getZ(), 0, 0, 0);
            world.playSound(source, explosionSound, SoundCategory.BLOCKS, 1f, 1f, false);
        });
        return true;
    }
}
