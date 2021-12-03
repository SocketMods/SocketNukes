package dev.socketmods.socketnukes.tileentity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class CommonTileEntity extends BlockEntity {

    protected ItemStackHandler itemHandler;

    protected LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public CommonTileEntity(BlockEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        itemHandler = createHandler();
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));

        super.load(state, nbt);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        handler.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    protected abstract ItemStackHandler createHandler();
}
