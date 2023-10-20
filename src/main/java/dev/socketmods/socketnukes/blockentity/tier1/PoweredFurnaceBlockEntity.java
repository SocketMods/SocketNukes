package dev.socketmods.socketnukes.blockentity.tier1;

import dev.socketmods.socketnukes.blockentity.PoweredRecipeMachine;
import dev.socketmods.socketnukes.container.tier1.PoweredFurnaceMenu;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * The Block Entity for the Powered Furnace.
 * It accepts RF / FE and smelts items.
 *
 * Tier 1 machine for faster processing of raw resources.
 *
 * @author Curle
 */
public class PoweredFurnaceBlockEntity extends PoweredRecipeMachine<SmeltingRecipe> {

    public PoweredFurnaceBlockEntity(BlockPos p_155229_, BlockState p_155230_) {
        super(SNRegistry.POWERED_FURNACE_BE.get(), p_155229_, p_155230_, RecipeType.SMELTING);
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

}
