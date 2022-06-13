package dev.socketmods.socketnukes.blockentity.tier1;

import dev.socketmods.socketnukes.capability.EnergyStorageWrapper;
import dev.socketmods.socketnukes.container.tier1.PoweredFurnaceMenu;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PoweredFurnaceBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    private final EnergyStorageWrapper energy = new EnergyStorageWrapper(15000, 1000);
    private final LazyOptional<IEnergyStorage> lazyEnergy = LazyOptional.of(() -> energy);

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 72;


    public PoweredFurnaceBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(SNRegistry.POWERED_FURNACE_BE.get(), p_155229_, p_155230_);

        this.data = new ContainerData() {
            @Override
            public int get(int p_39284_) {
                switch(p_39284_) {
                    case 0 -> { return PoweredFurnaceBlockEntity.this.progress; }
                    case 1 -> { return PoweredFurnaceBlockEntity.this.maxProgress; }
                    default -> { return 0; }
                }
            }

            @Override
            public void set(int idx, int val) {
                switch(idx) {
                    case 0 -> PoweredFurnaceBlockEntity.this.progress = val;
                    case 1 -> PoweredFurnaceBlockEntity.this.maxProgress = val;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("blockentity.socketmods.powered_furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new PoweredFurnaceMenu(p_39954_, p_39955_, this, data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return lazyItemHandler.cast();
        else if (cap == CapabilityEnergy.ENERGY)
            return lazyEnergy.cast();

        else return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyEnergy.invalidate();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.put("energy", energy.serializeNBT());
        tag.put("progress", IntTag.valueOf(progress));

        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
        energy.deserializeNBT(tag.getCompound("energy"));
        progress = tag.getInt("progress");
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PoweredFurnaceBlockEntity entity) {
        if (isConsumingEnergy(entity))
            entity.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> cap.extractEnergy(50, false));

        if (hasRecipe(entity)) {
            if (hasEnergy(entity)) {
                entity.progress++;
                setChanged(level, pos, state);
                if (entity.progress > entity.maxProgress) {
                    craftItem(entity);
                }
            }
        } else {
            entity.resetProgress();
            setChanged(level, pos, state);
        }

    }

    private static boolean isConsumingEnergy(PoweredFurnaceBlockEntity entity) {
        return entity.progress > 0;
    }

    private static boolean hasRecipe(PoweredFurnaceBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<SmeltingRecipe> match = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, inventory, level);

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getResultItem());
    }

    private static void craftItem(PoweredFurnaceBlockEntity entity) {
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        Optional<SmeltingRecipe> match = level.getRecipeManager()
                .getRecipeFor(RecipeType.SMELTING, inventory, level);

        if(match.isPresent()) {
            entity.itemHandler.extractItem(0,1, false);

            entity.itemHandler.setStackInSlot(1, new ItemStack(match.get().getResultItem().getItem(),
                    entity.itemHandler.getStackInSlot(3).getCount() + 1));

            entity.resetProgress();
        }
    }

    /**
     * Return whether the furnace has enough energy to continue to smelt a single item.
     * @param entity the furnace Block Entity
     */
    private static boolean hasEnergy(PoweredFurnaceBlockEntity entity) {
        return entity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0) > 50;
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(1).getItem() == output.getItem() || inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }
}
