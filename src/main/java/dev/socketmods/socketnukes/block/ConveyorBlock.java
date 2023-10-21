package dev.socketmods.socketnukes.block;

import dev.socketmods.socketnukes.blockentity.ConveyorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class ConveyorBlock extends Block implements EntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = EnumProperty.create("conveyor_half", Half.class);

    public ConveyorBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(FACING).add(HALF);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {

        Direction direction = pContext.getClickedFace();
        BlockPos blockpos = pContext.getClickedPos();

        Direction facing = pContext.getHorizontalDirection();
        
        Half half = null;
        for (Direction a : Direction.values()) {
            BlockState state = pContext.getLevel().getBlockState(blockpos.relative(a));
            if (state.getBlock() instanceof ConveyorBlock) {
                // we mustn't match the conveyor against the one "ahead" of it, so we need to duplicate these branches but exclude the switch case from the facing checks.
                switch (a) {
                    case NORTH -> {
                        if (facing == Direction.SOUTH || facing == Direction.EAST) {
                            half = Half.RIGHT;
                            state.setValue(HALF, Half.LEFT);
                        } else if(facing == Direction.WEST) {
                            half = Half.LEFT;
                            state.setValue(HALF, Half.RIGHT);
                        }
                    }

                    case EAST -> {
                        if (facing == Direction.SOUTH) {
                            half = Half.RIGHT;
                            state.setValue(HALF, Half.LEFT);
                        } else if(facing == Direction.NORTH || facing == Direction.WEST) {
                            half = Half.LEFT;
                            state.setValue(HALF, Half.RIGHT);
                        }
                    }

                    case SOUTH -> {
                        if (facing == Direction.EAST) {
                            half = Half.LEFT;
                            state.setValue(HALF, Half.RIGHT);
                        } else if(facing == Direction.NORTH || facing == Direction.WEST) {
                            half = Half.RIGHT;
                            state.setValue(HALF, Half.LEFT);
                        }
                    }

                    case WEST -> {
                        if (facing == Direction.SOUTH || facing == Direction.EAST) {
                            half = Half.RIGHT;
                            state.setValue(HALF, Half.LEFT);
                        } else if(facing == Direction.NORTH) {
                            half = Half.LEFT;
                            state.setValue(HALF, Half.RIGHT);
                        }
                    }
                }
            }
        }
        
        if (half == null) half = Half.LONE;

        return this.defaultBlockState().setValue(FACING, facing).setValue(HALF, half);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new ConveyorBlockEntity(pPos, pState);
    }

    public enum Half implements StringRepresentable {
        LEFT("left"),
        RIGHT("right"),
        LONE("lone");

        private final String name;

        private Half(String pName) {
            this.name = pName;
        }

        public String toString() {
            return this.name;
        }

        public String getSerializedName() {
            return this.name;
        }
    }
}
