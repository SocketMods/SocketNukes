package dev.socketmods.socketnukes.actors;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;

public abstract class Actor {

    /**
     * @return true when the Actor should die
     */
    public abstract boolean tick(ServerWorld world);

    public void onCreation(ServerWorld world) {

    }

    public void onRemoval(ServerWorld world) {

    }

    /**
     * @return the Actors Role this is used to deserialize the Actor later
     */
    public abstract Role<?> getRole();

    /**
     * @return serialize the data to NBT which is then used by Role to deserialize this instance.
     */
    public abstract CompoundNBT serialize(CompoundNBT nbt);

}
