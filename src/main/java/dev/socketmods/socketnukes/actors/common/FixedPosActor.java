package dev.socketmods.socketnukes.actors.common;

import dev.socketmods.socketnukes.actors.Actor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3d;

public abstract class FixedPosActor extends Actor {

    protected Vector3d position;

    public FixedPosActor(Vector3d position) {
        this.position = position;
    }

    public Vector3d getPosition() {
        return position;
    }

    @Override
    public CompoundNBT serialize(CompoundNBT nbt) {
        nbt.putDouble("X", position.getX());
        nbt.putDouble("Y", position.getY());
        nbt.putDouble("Z", position.getZ());
        return nbt;
    }

    public static Vector3d deserializePosition(CompoundNBT nbt) {
        return new Vector3d(nbt.getDouble("X"), nbt.getDouble("Y"), nbt.getDouble("Z"));
    }

}
