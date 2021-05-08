package dev.socketmods.socketnukes.actors.dummy;

import dev.socketmods.socketnukes.actors.Actor;
import dev.socketmods.socketnukes.actors.Role;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;

public class DummyActor extends Actor {

    @Override
    public boolean tick(ServerWorld world) {
        return true;
    }

    @Override
    public Role<?> getRole() {
        return SNRegistry.DUMMY_ROLE.get();
    }

    @Override
    public CompoundNBT serialize(CompoundNBT nbt) {
        return nbt;
    }
}
