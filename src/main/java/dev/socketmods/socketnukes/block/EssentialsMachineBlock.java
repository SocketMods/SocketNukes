package dev.socketmods.socketnukes.block;

import dev.socketmods.socketnukes.tileentity.EssentialsCommonTileEntity;
import dev.socketmods.socketnukes.tileentity.EssentialsMachineTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class EssentialsMachineBlock extends EssentialsCommonMachineBlock {

  public EssentialsMachineBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.FACING);
    builder.add(BlockStateProperties.LIT);
    super.fillStateContainer(builder);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {

    return getDefaultState()
        .with(BlockStateProperties.FACING, context.getPlacementHorizontalFacing().getOpposite())
        .with(BlockStateProperties.LIT, false);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (!state.isIn(newState.getBlock())) {
      TileEntity te = worldIn.getTileEntity(pos);
      if (te instanceof EssentialsCommonTileEntity) {
        LazyOptional<IItemHandler> inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (inventory.isPresent()) {
          dropInventoryItems(worldIn, pos, inventory.resolve().get());
        }
      }
    }
    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
    if (!worldIn.isRemote) {
      TileEntity tile = worldIn.getTileEntity(pos);
      if (tile instanceof EssentialsMachineTileEntity) {
        EssentialsMachineTileEntity CommonTe = (EssentialsMachineTileEntity) tile;
        NetworkHooks.openGui((ServerPlayerEntity) player, CommonTe, tile.getPos());
      } else {
        throw new IllegalStateException("Missing Container provider");
      }
    }

    return ActionResultType.SUCCESS;
  }

  public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
    if (stateIn.get(BlockStateProperties.LIT)) {
      double d0 = (double) pos.getX() + 0.5D;
      double d1 = pos.getY();
      double d2 = (double) pos.getZ() + 0.5D;
      if (rand.nextDouble() < 0.1D) {
        worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
      }

      Direction direction = stateIn.get(BlockStateProperties.FACING);
      Direction.Axis axis = direction.getAxis();
      double d3 = 0.52D;
      double d4 = rand.nextDouble() * 0.6D - 0.3D;
      double d5 = axis == Direction.Axis.X ? (double) direction.getXOffset() * 0.52D : d4;
      double d6 = rand.nextDouble() * 6.0D / 16.0D;
      double d7 = axis == Direction.Axis.Z ? (double) direction.getZOffset() * 0.52D : d4;
      worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
      worldIn.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
    }
  }

}
