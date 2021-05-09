package dev.socketmods.socketnukes.entity;

import javax.annotation.Nullable;

import dev.socketmods.socketnukes.actors.example.VisualActor;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class VisualActorEntity extends Entity {

    private final @Nullable VisualActor owner;

    public VisualActorEntity(EntityType<?> type, World world) {
        super(type, world);
        noClip = true;
        owner = null;
    }

    public VisualActorEntity(World world, VisualActor owner) {
        super(SNRegistry.VISUAL_TYPE.get(), world);
        this.owner = owner;
        noClip = true;
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (owner == null || owner.getLinkedEntity() != this) {
                if (owner == null) System.out.println("Null Owner"); else System.out.println(owner.getLinkedEntity());

                onKillCommand();
            }
        }

        super.tick();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
