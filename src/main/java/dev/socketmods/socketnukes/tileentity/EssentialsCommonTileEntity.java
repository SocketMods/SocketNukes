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

import javax.annotation.Nonnull;

public abstract class EssentialsCommonTileEntity extends TileEntity {

  protected ItemStackHandler itemHandler;

  protected LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

  public EssentialsCommonTileEntity(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
    itemHandler = createHandler();
  }

  @Override
  public void read(BlockState state, CompoundNBT nbt) {
    itemHandler.deserializeNBT(nbt.getCompound("inv"));

    super.read(state, nbt);
  }

  @Override
  public CompoundNBT write(CompoundNBT compound) {
    compound.put("inv", itemHandler.serializeNBT());
    return super.write(compound);
  }


  @Override
  public void remove() {
    super.remove();
    handler.invalidate();
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nonnull Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return handler.cast();
    }
    return super.getCapability(cap, side);
  }

  protected abstract ItemStackHandler createHandler();
}
