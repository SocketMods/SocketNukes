package dev.socketmods.socketnukes.block;

import dev.socketmods.socketnukes.blockentity.ConveyorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ConveyorBlock extends Block implements EntityBlock {

    public ConveyorBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ConveyorBlockEntity(pPos, pState);
    }
}
