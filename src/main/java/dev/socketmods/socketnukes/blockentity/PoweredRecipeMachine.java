package dev.socketmods.socketnukes.blockentity;

import dev.socketmods.socketnukes.capability.EnergyStorageWrapper;
import dev.socketmods.socketnukes.container.tier1.PoweredFurnaceMenu;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * An abstraction for Block Entities that take RF (via Forge Energy) and use standard recipes to determine outputs.
 *
 * Set up the Menu such that the input is slot 0, the output is slot 1.
 * A third slot is provided that can take power input, but this need not be exposed to the player.
 * Recipes that take multiple inputs can use other classes.
 *
 * @author Curle
 */
public abstract class PoweredRecipeMachine<T extends Recipe<Container>> extends BlockEntity implements MenuProvider {

    // Slot handler. 3 slots by default; input, output, power source.
    protected final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    protected final LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);

    // Energy capability.
    protected final EnergyStorageWrapper energy = new EnergyStorageWrapper(15000, 1000);
    protected final LazyOptional<IEnergyStorage> lazyEnergy = LazyOptional.of(() -> energy);

    protected final ContainerData data;
    // Recipe processing progress.
    protected int progress = 0;
    // Recipes take 72 ticks by default to progress.
    protected int maxProgress = 72;

    protected final RecipeType<T> recipeType;


    public PoweredRecipeMachine(BlockEntityType<?> type, BlockPos pos, BlockState state, RecipeType<T> recipe) {
        super(type, pos, state);

        recipeType = recipe;

        this.data = new ContainerData() {
            @Override
            public int get(int p_39284_) {
                switch(p_39284_) {
                    case 0 -> { return progress; }
                    case 1 -> { return maxProgress; }
                    default -> { return 0; }
                }
            }

            @Override
            public void set(int idx, int val) {
                switch(idx) {
                    case 0 -> progress = val;
                    case 1 -> maxProgress = val;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
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
        energy.deserializeNBT(tag.get("energy"));
        progress = tag.getInt("progress");
    }

    /**
     * The reference tick method.
     * Point the machine's Block at this method to inherit all of the processing done by this class.
     */
    public static void tick(Level level, BlockPos pos, BlockState state, PoweredRecipeMachine<?> entity) {
        entity.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {

            // If there's a suitable charging item in the port, and we can take all of its charge, do so and remove the item.
            if (entity.hasEnergyItem() && cap.getEnergyStored() + 5000 < cap.getMaxEnergyStored()) {
                ((EnergyStorageWrapper)cap).addEnergy(5000);
                entity.itemHandler.getStackInSlot(2).shrink(1);
            }


            // If there's a recipe available..
            if (entity.hasRecipe()) {
                // And we have the energy to process it..
                if ((entity.progress == 0 && entity.canProcessFullItem()) || entity.hasEnergy()) {

                    // Start (or continue) processing it
                    entity.progress++;
                    // If we're processing an item, consume 50 rf/t.
                    ((EnergyStorageWrapper)cap).consumeEnergy(50);
                    // Inform nearby blocks that we are, or have started, processing
                    setChanged(level, pos, state);

                    // Check if we're done processing
                    if (entity.progress > entity.maxProgress) {
                        // If we are, finish up and output the finished item.
                        entity.craftItem();
                    }
                }
            } else {
                // If there's no recipe, reset our progress just in case.
                entity.resetProgress();
                setChanged(level, pos, state);
            }
        });

    }

    /**
     * @return whether the inventory contains a power source item in the proper slot.
     */
    protected boolean hasEnergyItem() {
        return itemHandler.getStackInSlot(2).is(SNRegistry.POWER_SOURCE);
    }

    /**
     * @return whether the machine is currently processing an item and consuming energy.
     */
    public boolean isConsumingEnergy() {
        return progress > 0;
    }

    /**
     * @return whether the machine has enough energy in storage to fully process an item.
     */
    public boolean canProcessFullItem() {
        return energy.getEnergyStored() >= maxProgress * 50;
    }

    /**
     * @return whether the inputs for a valid recipe are in the inventory, and the output is compatible / has room for more.
     * TODO: this is essentially duplicating the logic below. Can we pull this out?
     */
    protected boolean hasRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<T> match = level.getRecipeManager()
                .getRecipeFor(recipeType, inventory, level);

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getResultItem());
    }

    /**
     * Process whatever is currently in the input slot and move it to the output slot.
     * If there's something already in the output slot, increase it by one.
     */
    protected void craftItem() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<T> match = level.getRecipeManager()
                .getRecipeFor(recipeType, inventory, level);

        if(match.isPresent()) {
            itemHandler.extractItem(0,1, false);
            if (itemHandler.getStackInSlot(1).getItem() == match.get().getResultItem().getItem())
                itemHandler.getStackInSlot(1).grow(1);
            else
                itemHandler.setStackInSlot(1, match.get().getResultItem());

            resetProgress();
        }
    }

    /**
     * Return whether the furnace has enough energy to continue to smelt a single item.
     */
    public boolean hasEnergy() {
        return getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0) > 50;
    }

    /**
     * Reset the progress meter to 0 once processing of an item has finished.
     */
    protected void resetProgress() {
        this.progress = 0;
    }

    /**
     * Check whether the given item in the given inventory is able to be placed in our output slot.
     * @param inventory the inventory of the BlockEntity containing the output slot
     * @param output the item we're attempting to put there
     * @return true if the item can be put in the slot, false otherwise.
     */
    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack output) {
        return inventory.getItem(1).getItem() == output.getItem() || inventory.getItem(1).isEmpty();
    }

    /**
     * Check whether more of the given item can be put in the output slot.
     * @param inventory the inventory of the BlockEntity containing the output slot.
     * @return true if more items can fit in the slot.
     */
    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }
}
