package dev.socketmods.socketnukes.actors.example;

import javax.annotation.Nullable;

import dev.socketmods.socketnukes.actors.Role;
import dev.socketmods.socketnukes.actors.common.PosActor;
import dev.socketmods.socketnukes.entity.VisualActorEntity;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

public class VisualActor extends PosActor {

    private @Nullable Entity linkedEntity;
    private int lifetime;

    public VisualActor(Vector3d position) {
        this(position, 1000);
    }

    public VisualActor(Vector3d position, int lifetime) {
        super(position);
        this.lifetime = lifetime;
    }

    @Override
    public boolean tick(ServerWorld world) {
        // Decrement our Lifeline
        lifetime--;

        if (lifetime % 200 == 0)
            world
                .getServer()
                .getPlayerList()
                .func_232641_a_(new StringTextComponent(lifetime + " ticks left until actual death!"), ChatType.SYSTEM, Util.DUMMY_UUID);

        // Recreate our entity if it's dead
        linkedEntity = maybeRecreateEntity(world);

        // Spawn Particles
        world.spawnParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, position.getX(), position.getY(), position.getZ(), 5, 0.5D, 0.5D, 0.5D, 0.5D);

        return lifetime < 0;
    }

    @Override
    public void onCreation(ServerWorld world) {
        linkedEntity = maybeRecreateEntity(world);
    }

    @Override
    public void onRemoval(ServerWorld world) {
        if (linkedEntity != null) linkedEntity.onKillCommand();
    }

    @Nullable
    private Entity maybeRecreateEntity(ServerWorld world) {
        if (linkedEntity == null) return createEntity(world);
        if (!linkedEntity.isAlive()) return createEntity(world);
        if (!world.isBlockLoaded(linkedEntity.getPosition())) return createEntity(world);

        return linkedEntity;
    }

    @Nullable
    private Entity createEntity(ServerWorld world) {
        if (linkedEntity != null) linkedEntity.onKillCommand();
        if (!world.isBlockLoaded(new BlockPos(position.x, position.y, position.z))) return null;

        world
            .getServer()
            .getPlayerList()
            .func_232641_a_(new StringTextComponent("Recreated Entity: " + lifetime + " ticks left until actual death!"), ChatType.SYSTEM, Util.DUMMY_UUID);

        Entity entity = new VisualActorEntity(world, this);
        entity.setPosition(position.x, position.y, position.z);
        world.addEntity(entity);

        return entity;
    }

    @Nullable
    public Entity getLinkedEntity() {
        return linkedEntity;
    }

    @Override
    public Role<?> getRole() {
        return SNRegistry.VISUAL_ROLE.get();
    }

    @Override
    public CompoundNBT serialize(CompoundNBT nbt) {
        super.serialize(nbt);

        nbt.putInt("lifetime", lifetime);

        return nbt;
    }

}
