package dev.socketmods.socketnukes.actors.common;

import dev.socketmods.socketnukes.actors.Actor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public abstract class BlockPosActor extends Actor {

    protected BlockPos position;

    public BlockPosActor(BlockPos position) {
        this.position = position;
    }

    public BlockPos getPosition() {
        return position;
    }

    @Override
    public CompoundNBT serialize(CompoundNBT nbt) {
        nbt.putInt("X", position.getX());
        nbt.putInt("Y", position.getY());
        nbt.putInt("Z", position.getZ());

        return nbt;
    }

    public static BlockPos deserializePosition(CompoundNBT nbt) {
        return NBTUtil.readBlockPos(nbt);
    }

}
