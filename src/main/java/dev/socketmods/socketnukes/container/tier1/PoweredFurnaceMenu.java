package dev.socketmods.socketnukes.container.tier1;

import dev.socketmods.socketnukes.blockentity.tier1.PoweredFurnaceBlockEntity;
import dev.socketmods.socketnukes.container.PoweredProcessorMenu;
import dev.socketmods.socketnukes.container.slot.PowerStorageSlot;
import dev.socketmods.socketnukes.container.slot.SimpleResultSlot;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class PoweredFurnaceMenu extends PoweredProcessorMenu<PoweredFurnaceBlockEntity> {
    public PoweredFurnaceMenu(int windowID, Inventory inv, PoweredFurnaceBlockEntity blockEntity, @Nullable ContainerData data) {
        super(windowID, inv, blockEntity, data, SNRegistry.POWERED_FURNACE_MENU.get());
    }

    public PoweredFurnaceMenu(int windowID, Inventory inv, FriendlyByteBuf buf) {
        this(windowID, inv, (PoweredFurnaceBlockEntity) inv.player.level().getBlockEntity(buf.readBlockPos()), null);
    }

    @Override
    protected void addMenuSlots() {
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(cap -> {
            this.addSlot(new SlotItemHandler(cap, 0, 66, 33));
            this.addSlot(new SimpleResultSlot(cap, 1, 114, 33));
            this.addSlot(new PowerStorageSlot(cap, 2, 8, 44));
        });
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), p_38874_, SNRegistry.POWERED_FURNACE_BLOCK.get());
    }
}