package dev.socketmods.socketnukes.container;

import dev.socketmods.socketnukes.blockentity.PoweredRecipeMachine;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

/**
 * The abstract Menu used for Powered Recipe Machines that take RF and process a single item into a single output.
 * Handles all of the inventory management, just give it the slots and positions of the machine via the ContainerData field.
 *
 * @param <T> the Machine that this Menu is processing for.
 *
 * @author Curle
 */
public abstract class PoweredProcessorMenu<T extends PoweredRecipeMachine<?>> extends AbstractContainerMenu {
    protected final T blockEntity;
    protected final Level level;
    protected final ContainerData data;

    protected final int HOTBAR_SLOT_COUNT = 9;
    protected final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    protected final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    protected final int TE_INVENTORY_SLOT_COUNT = 3;

    public PoweredProcessorMenu(int windowID, Inventory inv, T blockEntity, @Nullable ContainerData data, MenuType<?> type) {
        super(type, windowID);
        this.blockEntity = blockEntity;
        this.level = inv.player.level();
        this.data = data == null ? blockEntity.data : data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        addMenuSlots();
    }


    /**
     * Add the Slots for this Menu here.
     * Defines the layout and position on screen, as well as what kind of item can go into those slots.
     */
    protected abstract void addMenuSlots();

    public int getEnergyStored() {
        return blockEntity.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    public int getMaxEnergy() {
        return blockEntity.getCapability(ForgeCapabilities.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0);
    }

    public boolean isSmelting() {
        return blockEntity.isConsumingEnergy();
    }

    public int getScaledProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false))
                return ItemStack.EMPTY;  // EMPTY_ITEM
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false))
                return ItemStack.EMPTY;
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0)
            sourceSlot.set(ItemStack.EMPTY);
        else
            sourceSlot.setChanged();

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    private void addPlayerInventory(Inventory playerInv) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInv) {
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInv, i, 8 + i * 18, 144));
        }
    }
}
