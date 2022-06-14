package dev.socketmods.socketnukes.container.slot;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class PowerStorageSlot extends SlotItemHandler {

    public PowerStorageSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(SNRegistry.POWER_SOURCE);
    }
}
