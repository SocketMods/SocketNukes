package dev.socketmods.socketnukes.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import org.jetbrains.annotations.Nullable;

public abstract class CommonMachineBlock extends Block implements EntityBlock {

    public CommonMachineBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public abstract InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player,
        InteractionHand handIn, BlockHitResult hit);

    protected static void dropInventoryItems(Level worldIn, BlockPos pos, IItemHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack itemstack = inventory.getStackInSlot(i);

            if (itemstack.getCount() > 0) {
                Containers.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), itemstack);
            }
        }
    }
}
