package dev.socketmods.socketnukes.actors.example;

import dev.socketmods.socketnukes.actors.Actor;
import dev.socketmods.socketnukes.actors.Role;
import dev.socketmods.socketnukes.actors.common.BlockPosActor;
import net.minecraft.nbt.CompoundNBT;

public class BolbSpawnRole extends Role<Actor> {

    @Override
    public Actor deserialize(CompoundNBT nbt) {
        return new BolbSpawnActor(
            BlockPosActor.deserializePosition(nbt),
            nbt.getInt("count"),
            nbt.getInt("triggers"),
            nbt.getInt("cooldown"),
            nbt.getInt("timer")
        );
    }

}
