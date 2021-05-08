package dev.socketmods.socketnukes.actors.example;

import dev.socketmods.socketnukes.actors.Actor;
import dev.socketmods.socketnukes.actors.Role;
import dev.socketmods.socketnukes.actors.common.FixedBlockPosActor;
import net.minecraft.nbt.CompoundNBT;

public class BolbSpawnRole extends Role<Actor> {

    @Override
    public Actor deserialize(CompoundNBT nbt) {
        return new BolbSpawnActor(
            FixedBlockPosActor.deserializePosition(nbt),
            nbt.getInt("count"),
            nbt.getInt("triggers"),
            nbt.getInt("cooldown"),
            nbt.getInt("timer")
        );
    }

}
