package dev.socketmods.socketnukes.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public abstract class CommonTileEntity extends BlockEntity {

    protected ItemStackHandler itemHandler;

    protected LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public CommonTileEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos blockPos, BlockState blockState) {
        super(tileEntityTypeIn, blockPos, blockState);
        itemHandler = createHandler();
    }

    @Override
    public void load(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));

        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        compound.put("inv", itemHandler.serializeNBT());
        super.saveAdditional(compound);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    protected abstract ItemStackHandler createHandler();
}
