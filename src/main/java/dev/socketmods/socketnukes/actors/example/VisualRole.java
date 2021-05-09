package dev.socketmods.socketnukes.actors.example;

import dev.socketmods.socketnukes.actors.Role;
import dev.socketmods.socketnukes.actors.common.PosActor;
import net.minecraft.nbt.CompoundNBT;

public class VisualRole extends Role<VisualActor> {

    @Override
    public VisualActor deserialize(CompoundNBT nbt) {
        return new VisualActor(
            PosActor.deserializePosition(nbt),
            nbt.getInt("lifetime")
        );
    }
}
