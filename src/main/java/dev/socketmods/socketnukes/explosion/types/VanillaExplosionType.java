package dev.socketmods.socketnukes.explosion.types;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import dev.socketmods.socketnukes.explosion.DummyExplosion;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
 import net.minecraft.block.*;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;

import java.util.*;

import net.minecraft.world.Explosion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

public class VanillaExplosionType extends ExtendedExplosionType {
    private ExplosionProperties properties;

    private static final int STAGE_DAMAGE = 1;
    private static final int STAGE_BLOCKS = 2;
    private static final int STAGE_FLUIDS = 3;

    public VanillaExplosionType(ExplosionProperties properties) {
        super(4, Arrays.asList(Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN),
                3, DamageSource.GENERIC, false);

        this.properties = properties;

    }

    @Override
    public boolean prepareExplosion(World worldIn, BlockPos source, Entity placer) {
        return true;
    }

    @Override
    public List<BlockPos> explode(World worldIn, BlockPos sourceOld, int stage, Entity placer, List<BlockPos> blocksFromLastState) {
        List<BlockPos> affectedBlocks = (blocksFromLastState.size() == 0) ? new ArrayList<>() : blocksFromLastState;
        Map<Entity, Vector3d> entityDisplacements = new HashMap<>();
        BlockPos source = new BlockPos(sourceOld.getX(), sourceOld.getY() + 1, sourceOld.getZ());

        switch(stage) {
            case STAGE_DAMAGE:
                // Damage entities, calculate blocks to be broken
                Set<BlockPos> set = Sets.newHashSet();
                Explosion vanillaExplosion = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), this);
                for (int j = 0; j < 16; ++j) {
                    for (int k = 0; k < 16; ++k) {
                        for (int l = 0; l < 16; ++l) {
                            if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                                double d0 = (float) j / 15.0F * 2.0F - 1.0F;
                                double d1 = (float) k / 15.0F * 2.0F - 1.0F;
                                double d2 = (float) l / 15.0F * 2.0F - 1.0F;
                                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                d0 = d0 / d3;
                                d1 = d1 / d3;
                                d2 = d2 / d3;
                                float f = radius * (0.7F + worldIn.rand.nextFloat() * 0.6F);
                                double d4 = source.getX();
                                double d6 = source.getY();
                                double d8 = source.getZ();

                                for (; f > 0.0F; f -= 0.22500001F) {
                                    BlockPos blockpos = new BlockPos(d4, d6, d8);
                                    BlockState blockstate = worldIn.getBlockState(blockpos);
                                    FluidState fluidstate = worldIn.getFluidState(blockpos);

                                    ExplosionContext dummyContext = new ExplosionContext();

                                    Optional<Float> optional = dummyContext.getExplosionResistance(vanillaExplosion, worldIn, blockpos, blockstate, fluidstate);
                                    if (optional.isPresent()) {
                                        f -= (optional.get() + 0.3F) * 0.3F;
                                    }

                                    if (f > 0.0F/* && !immuneBlocks.contains(worldIn.getBlockState(blockpos).getBlock())*/) {
                                        set.add(blockpos);
                                    }

                                    d4 += d0 * (double) 0.3F;
                                    d6 += d1 * (double) 0.3F;
                                    d8 += d2 * (double) 0.3F;
                                }
                            }
                        }
                    }
                }

                affectedBlocks.addAll(set);
                float radiusx2 = radius * 2.0F;
                int eastBound = MathHelper.floor(source.getX() - (double) radiusx2 - 1.0D);
                int westBound = MathHelper.floor(source.getX() + (double) radiusx2 + 1.0D);
                int lowerBound = MathHelper.floor(source.getY() - (double) radiusx2 - 1.0D);
                int upperBound = MathHelper.floor(source.getY() + (double) radiusx2 + 1.0D);
                int southBound = MathHelper.floor(source.getZ() - (double) radiusx2 - 1.0D);
                int northBound = MathHelper.floor(source.getZ() + (double) radiusx2 + 1.0D);

                List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(placer, new AxisAlignedBB(eastBound, lowerBound, southBound, westBound, upperBound, northBound));

                vanillaExplosion = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radiusx2, affectedBlocks);
                ForgeEventFactory.onExplosionDetonate(worldIn, vanillaExplosion, list, radiusx2);
                Vector3d explosionPos = new Vector3d(source.getX(), source.getY(), source.getZ());

                for (Entity currentEntity : list) {
                    if (!currentEntity.isImmuneToExplosions()) {
                        double currentEntityDistanceToExplosion = MathHelper.sqrt(currentEntity.getDistanceSq(explosionPos)) / radiusx2;
                        if (currentEntityDistanceToExplosion <= 1.0D) {
                            double currentEntityDistanceX = currentEntity.getPosX() - source.getX();
                            double currentEntityDistanceY = (currentEntity instanceof TNTEntity ? currentEntity.getPosY() : currentEntity.getPosYEye()) - source.getY();
                            double currentEntityDistanceZ = currentEntity.getPosZ() - source.getZ();
                            double pythagoreanDistance = MathHelper.sqrt(currentEntityDistanceX * currentEntityDistanceX + currentEntityDistanceY * currentEntityDistanceY + currentEntityDistanceZ * currentEntityDistanceZ);
                            if (pythagoreanDistance != 0.0D) {
                                currentEntityDistanceX = currentEntityDistanceX / pythagoreanDistance;
                                currentEntityDistanceY = currentEntityDistanceY / pythagoreanDistance;
                                currentEntityDistanceZ = currentEntityDistanceZ / pythagoreanDistance;
                                double rayLength = Explosion.getBlockDensity(explosionPos, currentEntity);
                                double damageFalloff = (1.0D - currentEntityDistanceToExplosion) * rayLength;
                                currentEntity.attackEntityFrom(this.getDamageSource(), (float) ((int) ((damageFalloff * damageFalloff + damageFalloff) / 2.0D * 7.0D * (double) radiusx2 + 1.0D)));
                                if (currentEntity instanceof LivingEntity) {
                                    damageFalloff = ProtectionEnchantment.getBlastDamageReduction((LivingEntity) currentEntity, damageFalloff);
                                }

                                currentEntity.setMotion(currentEntity.getMotion().add(currentEntityDistanceX * damageFalloff, currentEntityDistanceY * damageFalloff, currentEntityDistanceZ * damageFalloff));
                                if (currentEntity instanceof PlayerEntity) {
                                    PlayerEntity playerentity = (PlayerEntity) currentEntity;
                                    if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.isFlying)) {
                                        entityDisplacements.put(playerentity, new Vector3d(currentEntityDistanceX * damageFalloff, currentEntityDistanceY * damageFalloff, currentEntityDistanceZ * damageFalloff));
                                    }
                                }
                            }
                        }
                    }
                }
                return affectedBlocks;
            case STAGE_BLOCKS:
                if (worldIn.isRemote) {
                    worldIn.playSound(source.getX(), source.getY(), source.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F, false);
                }

                if (properties.doesMakeParticles()) {
                    worldIn.addParticle(properties.getParticleToEmit(), source.getX(), source.getY(), source.getZ(), 1.0D, 0.0D, 0.0D);
                }

                if (doBlocksDrop) {
                    ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
                    Collections.shuffle(affectedBlocks, worldIn.rand);

                    for (BlockPos blockpos : affectedBlocks) {
                        BlockState blockstate = worldIn.getBlockState(blockpos);
                        if (!blockstate.isAir(worldIn, blockpos)) {
                            BlockPos blockpos1 = blockpos.toImmutable();
                            worldIn.getProfiler().startSection("explosion_blocks");

                            Explosion vanillaExplosion2 = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radius, affectedBlocks);

                            if (blockstate.canDropFromExplosion(worldIn, blockpos, vanillaExplosion2) && worldIn instanceof ServerWorld) {
                                TileEntity tileentity = blockstate.hasTileEntity() ? worldIn.getTileEntity(blockpos) : null;
                                LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) worldIn)).withRandom(worldIn.rand).withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(blockpos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withNullableParameter(LootParameters.BLOCK_ENTITY, tileentity).withNullableParameter(LootParameters.THIS_ENTITY, placer);
                                if (this.doBlocksDrop) {
                                    lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, (float) radius);
                                }

                                blockstate.getDrops(lootcontext$builder).forEach((stack) -> {
                                    handleExplosionDrops(objectarraylist, stack, blockpos1);
                                });
                            }
                            if(!worldIn.isRemote)
                                worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 1 + 2 + 8);
                                //blockstate.onBlockExploded(worldIn, blockpos, vanillaExplosion2);
                            worldIn.getProfiler().endSection();
                        }
                    }

                    for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                        if(!worldIn.isRemote)
                            Block.spawnAsEntity(worldIn, pair.getSecond(), pair.getFirst());
                    }
                }

                if (properties.causesFire()) {
                    for (BlockPos blockpos2 : affectedBlocks) {
                        if (worldIn.rand.nextInt(3) == 0 && worldIn.getBlockState(blockpos2).isAir() && worldIn.getBlockState(blockpos2.down()).isOpaqueCube(worldIn, blockpos2.down())) {
                            if(!worldIn.isRemote)
                                worldIn.setBlockState(blockpos2, AbstractFireBlock.getFireForPlacement(worldIn, blockpos2));
                        }
                    }
                }

                return affectedBlocks;

            case STAGE_FLUIDS:
                for(BlockPos pos : affectedBlocks) {
                    BlockState state = worldIn.getBlockState(pos);

                    if (!state.isAir(worldIn, pos)) {
                        state.neighborChanged(worldIn, pos, state.getBlock(), sourceOld, false);
                    }
                }

            default:
                // Handle unknown stage? maybe? perhaps?
                return affectedBlocks;
        }
    }

    private static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos) {
        int i = dropPositionArray.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.canMergeStacks(itemstack, stack)) {
                ItemStack itemstack1 = ItemEntity.mergeStacks(itemstack, stack, 16);
                dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        dropPositionArray.add(Pair.of(stack, pos));
    }
}
