package dev.socketmods.socketnukes.blockentity;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ConveyorBlockEntity extends BlockEntity {

    public ConveyorBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(SNRegistry.CONVEYOR_BE.get(), pPos, pBlockState);
    }
}
