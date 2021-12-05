package dev.socketmods.socketnukes.block.explosive;

import javax.annotation.Nullable;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.entity.ExplosiveEntity;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import dev.socketmods.socketnukes.tileentity.ExplosiveTileEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

/**
 * The explosive block used for testing.
 * It can trigger, on ignition, the vanilla-like explosion by default.
 * When right clicked with a configured Exploder Item, it will create an explosion alike the configured item.
 *
 * That meaning, if you right click this block with an Exploder configured to "Cubic", it will create a cubic explosion when it pops.
 *
 * It can be passed any ExtendedExplosionType for this purpose.
 *
 * @author Citrine
 */
public class TNTExplosive extends BaseEntityBlock {
    public TNTExplosive() {
        super(BlockBehaviour.Properties.of(Material.EXPLOSIVE));
    }

    /**
     * Sets the short-lived tile entity data when this block is placed by the BlockEntity.
     * This is just a hacky way of persisting the data inside the block.
     * This should use WorldSavedData or something.
     * @param world The world where the block was placed
     * @param pos The position the block was placed at
     * @param state The new blockstate of this block.
     * @param placer The entity that placed the block
     * @param stack The stack the entity was holding when it was placed.
     */
    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);

        BlockEntity tileEntity = world.getBlockEntity(pos);
        if(stack.getItem() == SNRegistry.GENERIC_EXPLOSIVE_ITEM.get() && tileEntity instanceof ExplosiveTileEntity) {
            ExplosiveTileEntity explosive = (ExplosiveTileEntity) tileEntity;

            ResourceLocation config = new ResourceLocation(stack.getOrCreateTagElement(SocketNukes.MODID).getString("explosion"));
            explosive.setConfiguration(config);
        }
    }

    /**
     * Surrounding logic for the explosion.
     * Called when this block catches fire, or when it is ignited with flint and steel.
     * @param state the blockstate of the target (this block)
     * @param world the world the block is in
     * @param pos the position of the block
     * @param face the face of the block that was ignited
     * @param igniter the entity that performed the ignition. Usually either the player or Lightning.
     */
    @Override
    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @org.jetbrains.annotations.Nullable Direction face, @org.jetbrains.annotations.Nullable LivingEntity igniter) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if(tileEntity instanceof ExplosiveTileEntity) {
            ExplosiveTileEntity explosive = (ExplosiveTileEntity) tileEntity;
            ResourceLocation config = explosive.getConfiguration();
            ExtendedExplosionType explosion = SNRegistry.getExplosionType(config);

            explode(world, pos, igniter, explosion);
        } else {
            explode(world, pos, igniter, SNRegistry.VANILLA_EXPLOSION.get());
        }
    }

    /**
     * Called when this block is placed.
     * Since TNT ignites when it is placed next to redstone power, so too do we.
     * @param state the new state of the block after placement
     * @param worldIn the world the block was placed into
     * @param pos the position the block was placed at
     * @param oldState the state of the block that this one took the place of.
     * @param isMoving whether this block was added by movement, ie. by a piston
     */
    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            if (worldIn.hasNeighborSignal(pos)) {
                onCaughtFire(state, worldIn, pos, null, null);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    /**
     * Called when a block updates around this one.
     * When a block around TNT is powered, the TNT ignites. So too do we.
     * @param state the new state of this block
     * @param worldIn the world the block is in
     * @param pos the position of this block
     * @param blockIn the class of the updated block
     * @param fromPos the position of the updated block
     * @param isMoving whether this block was updated by movement (ie. a piston)
     */
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (worldIn.hasNeighborSignal(pos)) {
            onCaughtFire(state, worldIn, pos, null, null);
            worldIn.removeBlock(pos, false);
        }
    }

    /**
     * Performs the actual explosion itself, by creating and spawning an explosive entity.
     * It flashes, like the TNT entity, but in a blue color.
     * Once the time is up, the configured explosion is invoked.
     * @param worldIn the world to spawn the entity into
     * @param pos the position to spawn the entity at
     * @param entityIn the igniter of this block
     * @param explosion the explosion to invoke when the entity pops
     * @param chainExplosion whether this explosion was triggered by another nearby
     */
    private static void explode(Level worldIn, BlockPos pos, @Nullable LivingEntity entityIn, ExtendedExplosionType explosion, boolean chainExplosion) {
        ExplosiveEntity explosiveEntity = new ExplosiveEntity(worldIn, pos, explosion, entityIn);
        worldIn.addFreshEntity(explosiveEntity);

        if(chainExplosion)
            explosiveEntity.setFuse((short)(worldIn.random.nextInt(explosion.getFuseTime() / 4) + explosion.getFuseTime() / 8));
        else
            explosiveEntity.setFuse(explosion.getFuseTime());

        worldIn.playSound(null, explosiveEntity.getX(), explosiveEntity.getY(), explosiveEntity.getZ(),
                SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    /**
     * An overload for the above, with default flags set.
     * @param worldin the world to trigger the explosion in
     * @param pos the position the explosion should happen in
     * @param entity the entity that triggered the explosion
     * @param type the explosion to trigger
     */
    private static void explode(Level worldin, BlockPos pos, @Nullable LivingEntity entity, ExtendedExplosionType type) {
        explode(worldin, pos, entity, type, false);
    }

    /**
     * Called when this block is destroyed by an explosion.
     * TNT ignites when this happens, so too do we.
     * TNT has a shorter fuse when it is ignited this way.
     * TODO: replicate this?
     * @param worldIn the world this block is in
     * @param pos the position this block is at
     * @param explosionIn the explosion that destroyed the block
     */
    @Override
    public void wasExploded(Level worldIn, BlockPos pos, Explosion explosionIn) {
        explode(worldIn, pos, explosionIn.getSourceMob(), SNRegistry.VANILLA_EXPLOSION.get(), true);
    }

    /**
     * Called when this block is used by a player holding an item.
     * TNT is ignited when it is used with Flint and Steel, or a Fire Charge. So too do we.
     * We also ignite when we are used with a configured Exploder Item.
     * @param state the current block
     * @param worldIn the world this block is in
     * @param pos the position this block is at
     * @param player the player that activated the block
     * @param handIn the hand that activated the block
     * @param hit context for how the block was hit
     * @return an action result
     */
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand
            handIn, BlockHitResult hit) {
        ItemStack itemstack = player.getItemInHand(handIn);
        Item item = itemstack.getItem();

        // If used by an Exploder..
        if(item == SNRegistry.EXPLODER_ITEM.get()) {
            // that is configured..
            itemstack.getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap ->
                    // create an explosive to mimic it.
                    explode(worldIn, pos, player, SNRegistry.getExplosionType(cap.getConfig()))
            );
            // delete the block because the entity was created
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            return InteractionResult.sidedSuccess(worldIn.isClientSide);

        // Fallback to vanilla behavior
        } else if (item != Items.FLINT_AND_STEEL && item != Items.FIRE_CHARGE) {
            return super.use(state, worldIn, pos, player, handIn, hit);
        } else {
            onCaughtFire(state, worldIn, pos, hit.getDirection(), player);
            worldIn.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            if (!player.isCreative()) {
                if (item == Items.FLINT_AND_STEEL) {
                    itemstack.hurtAndBreak(1, player, (player1) -> player1.broadcastBreakEvent(handIn));
                } else {
                    itemstack.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(worldIn.isClientSide);
        }
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState state) {
        return new ExplosiveTileEntity(blockPos, state);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return null;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public <T extends BlockEntity> GameEventListener getListener(Level p_153210_, T p_153211_) {
        return null;
    }
}
