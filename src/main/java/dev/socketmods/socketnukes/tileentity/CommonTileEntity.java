package dev.socketmods.socketnukes.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public abstract class CommonTileEntity extends TileEntity {

    protected ItemStackHandler itemHandler;

    protected LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public CommonTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        itemHandler = createHandler();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
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
