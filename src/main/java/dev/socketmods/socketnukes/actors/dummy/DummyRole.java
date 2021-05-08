package dev.socketmods.socketnukes.actors.dummy;

import dev.socketmods.socketnukes.actors.Actor;
import dev.socketmods.socketnukes.actors.Role;
import net.minecraft.nbt.CompoundNBT;

public class DummyRole extends Role<Actor> {

    @Override
    public Actor deserialize(CompoundNBT nbt) {
        return new DummyActor();
    }

}
