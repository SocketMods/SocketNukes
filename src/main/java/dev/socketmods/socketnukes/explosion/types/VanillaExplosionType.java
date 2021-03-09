package dev.socketmods.socketnukes.explosion.types;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import dev.socketmods.socketnukes.explosion.DummyExplosion;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import dev.socketmods.socketnukes.networking.Network;
import dev.socketmods.socketnukes.networking.packet.ExtendedExplosionPacket;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.ExplosionContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.*;

/**
 * Tries to mimic TNT as much as possible.
 * When triggered from an item, this seems to leave water in a broken state.
 * When triggered from an entity, it seems to work fine.
 * TODO: fix that.
 *
 * Otherwise, it serves as an example to the extensibility of the system, with a three-stage process.
 *
 * @author Curle, Citrine
 */
public class VanillaExplosionType extends ExtendedExplosionType {
    private final ExplosionProperties properties;

    private static final int STAGE_DAMAGE = 1;
    private static final int STAGE_BLOCKS = 2;
    private static final int STAGE_SYNC = 3;

    public VanillaExplosionType(ExplosionProperties properties) {
        super(4, Arrays.asList(Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN),
                3, DamageSource.GENERIC, false);
        this.properties = properties;
        this.setFuseTime(80);
    }

    @Override
    public boolean prepareExplosion(World worldIn, BlockPos source, Entity placer) {
        return true;
    }

    @Override
    public ExplosionMetaPackage explode(World worldIn, BlockPos sourceOld, int stage, Entity placer, ExplosionMetaPackage meta) {
        BlockPos source = new BlockPos(sourceOld.getX(), sourceOld.getY() + 1, sourceOld.getZ());

        switch(stage) {
            case STAGE_DAMAGE:
                // Damage entities, calculate blocks to be broken
                Set<BlockPos> set = Sets.newHashSet();
                Explosion vanillaExplosion = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), this);

                for (int xPos = 0; xPos < 16; ++xPos) {
                    for (int yPos = 0; yPos < 16; ++yPos) {
                        for (int zPos = 0; zPos < 16; ++zPos) {
                            if (xPos == 0 || xPos == 15 || yPos == 0 || yPos == 15 || zPos == 0 || zPos == 15) {
                                double xTemp = (float) xPos / 15.0F * 2.0F - 1.0F;
                                double yTemp = (float) yPos / 15.0F * 2.0F - 1.0F;
                                double zTemp = (float) zPos / 15.0F * 2.0F - 1.0F;
                                double avgDist = Math.sqrt(xTemp * xTemp + yTemp * yTemp + zTemp * zTemp);
                                xTemp = xTemp / avgDist;
                                yTemp = yTemp / avgDist;
                                zTemp = zTemp / avgDist;
                                float rayAngle = radius * (0.7F + worldIn.rand.nextFloat() * 0.6F);
                                double sourceX = source.getX();
                                double sourceY = source.getY();
                                double sourceZ = source.getZ();

                                for (; rayAngle > 0.0F; rayAngle -= 0.22500001F) {
                                    BlockPos blockpos = new BlockPos(sourceX, sourceY, sourceZ);
                                    BlockState blockstate = worldIn.getBlockState(blockpos);
                                    FluidState fluidstate = worldIn.getFluidState(blockpos);

                                    ExplosionContext dummyContext = new ExplosionContext();

                                    Optional<Float> optional = dummyContext.getExplosionResistance(vanillaExplosion, worldIn, blockpos, blockstate, fluidstate);
                                    if (optional.isPresent()) {
                                        rayAngle -= (optional.get() + 0.3F) * 0.3F;
                                    }

                                    if (rayAngle > 0.0F) {
                                        set.add(blockpos);
                                    }

                                    sourceX += xTemp * (double) 0.3F;
                                    sourceY += yTemp * (double) 0.3F;
                                    sourceZ += zTemp * (double) 0.3F;
                                }
                            }
                        }
                    }
                }

                meta.affectedBlocks.addAll(set);
                float radiusx2 = radius * 2.0F;
                int eastBound = MathHelper.floor(source.getX() - (double) radiusx2 - 1.0D);
                int westBound = MathHelper.floor(source.getX() + (double) radiusx2 + 1.0D);
                int lowerBound = MathHelper.floor(source.getY() - (double) radiusx2 - 1.0D);
                int upperBound = MathHelper.floor(source.getY() + (double) radiusx2 + 1.0D);
                int southBound = MathHelper.floor(source.getZ() - (double) radiusx2 - 1.0D);
                int northBound = MathHelper.floor(source.getZ() + (double) radiusx2 + 1.0D);

                List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(placer, new AxisAlignedBB(eastBound, lowerBound, southBound, westBound, upperBound, northBound));

                vanillaExplosion = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radiusx2, meta.affectedBlocks);
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
                                        meta.entityDisplacements.put(playerentity, new Vector3d(currentEntityDistanceX * damageFalloff, currentEntityDistanceY * damageFalloff, currentEntityDistanceZ * damageFalloff));
                                    }
                                }
                            }
                        }
                    }
                }
                return meta;
            case STAGE_BLOCKS:
                if (worldIn.isRemote) {
                    worldIn.playSound(source.getX(), source.getY(), source.getZ(), properties.getExplosionSound(), SoundCategory.BLOCKS, 4.0F, (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F, false);
                }

                if (properties.doesMakeParticles()) {
                    worldIn.addParticle(properties.getParticleToEmit(), source.getX(), source.getY(), source.getZ(), 1.0D, 0.0D, 0.0D);
                }

                if (doBlocksDrop) {
                    ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
                    Collections.shuffle(meta.affectedBlocks, worldIn.rand);

                    for (BlockPos blockpos : meta.affectedBlocks) {
                        BlockState blockstate = worldIn.getBlockState(blockpos);
                        if (!blockstate.isAir(worldIn, blockpos)) {
                            BlockPos blockpos1 = blockpos.toImmutable();
                            worldIn.getProfiler().startSection("explosion_blocks");

                            Explosion vanillaExplosion2 = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radius, meta.affectedBlocks);

                            if (blockstate.canDropFromExplosion(worldIn, blockpos, vanillaExplosion2) && worldIn instanceof ServerWorld) {
                                TileEntity tileentity = blockstate.hasTileEntity() ? worldIn.getTileEntity(blockpos) : null;
                                LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) worldIn)).withRandom(worldIn.rand).withParameter(LootParameters.field_237457_g_, Vector3d.copyCentered(blockpos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withNullableParameter(LootParameters.BLOCK_ENTITY, tileentity).withNullableParameter(LootParameters.THIS_ENTITY, placer);
                                if (this.doBlocksDrop) {
                                    lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, (float) radius);
                                }

                                blockstate.getDrops(lootcontext$builder).forEach((stack) -> handleExplosionDrops(objectarraylist, stack, blockpos1));
                            }
                            if(!worldIn.isRemote)
                                blockstate.onBlockExploded(worldIn, blockpos, vanillaExplosion2);
                            worldIn.getProfiler().endSection();
                        }
                    }

                    for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                        if(!worldIn.isRemote)
                            Block.spawnAsEntity(worldIn, pair.getSecond(), pair.getFirst());
                    }
                }

                if (properties.causesFire()) {
                    for (BlockPos blockpos2 : meta.affectedBlocks) {
                        if (worldIn.rand.nextInt(3) == 0 && worldIn.getBlockState(blockpos2).isAir() && worldIn.getBlockState(blockpos2.down()).isOpaqueCube(worldIn, blockpos2.down())) {
                            if(!worldIn.isRemote)
                                worldIn.setBlockState(blockpos2, AbstractFireBlock.getFireForPlacement(worldIn, blockpos2));
                        }
                    }
                }

                return meta;

            case STAGE_SYNC:
                // We have to manually tell the client that these blocks have broken, as onBlockExploded does not.
                if(!worldIn.isRemote) {
                    ServerWorld sWorld = (ServerWorld) worldIn;
                    for (ServerPlayerEntity serverplayerentity : sWorld.getPlayers()) {
                        if (serverplayerentity.getDistanceSq(source.getX(), source.getY(), source.getZ()) < 4096.0D) {
                            Network.sendToClient(new ExtendedExplosionPacket(source, meta.affectedBlocks, properties.getParticleToEmit().getType(), properties.getExplosionSound(), meta.entityDisplacements), serverplayerentity);
                        }
                    }
                }

            default:
                // Handle unknown stage? maybe? perhaps?
                return meta;
        }
    }

}
