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
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
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
                3, null, false);
        this.properties = properties;
        this.setFuseTime(80);
    }

    @Override
    public boolean prepareExplosion(Level worldIn, BlockPos source, Entity placer) {
        return true;
    }

    @Override
    public ExplosionMetaPackage explode(Level worldIn, BlockPos sourceOld, int stage, Entity placer, ExplosionMetaPackage meta) {
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
                                float rayAngle = radius * (0.7F + worldIn.random.nextFloat() * 0.6F);
                                int sourceX = source.getX();
                                int sourceY = source.getY();
                                int sourceZ = source.getZ();

                                for (; rayAngle > 0.0F; rayAngle -= 0.22500001F) {
                                    BlockPos blockpos = new BlockPos(sourceX, sourceY, sourceZ);
                                    BlockState blockstate = worldIn.getBlockState(blockpos);
                                    FluidState fluidstate = worldIn.getFluidState(blockpos);

                                    ExplosionDamageCalculator dummyContext = new ExplosionDamageCalculator();

                                    Optional<Float> optional = dummyContext.getBlockExplosionResistance(vanillaExplosion, worldIn, blockpos, blockstate, fluidstate);
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
                int eastBound = Mth.floor(source.getX() - (double) radiusx2 - 1.0D);
                int westBound = Mth.floor(source.getX() + (double) radiusx2 + 1.0D);
                int lowerBound = Mth.floor(source.getY() - (double) radiusx2 - 1.0D);
                int upperBound = Mth.floor(source.getY() + (double) radiusx2 + 1.0D);
                int southBound = Mth.floor(source.getZ() - (double) radiusx2 - 1.0D);
                int northBound = Mth.floor(source.getZ() + (double) radiusx2 + 1.0D);

                List<Entity> list = worldIn.getEntities(placer, new AABB(eastBound, lowerBound, southBound, westBound, upperBound, northBound));

                vanillaExplosion = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radiusx2, meta.affectedBlocks);
                ForgeEventFactory.onExplosionDetonate(worldIn, vanillaExplosion, list, radiusx2);
                Vec3 explosionPos = new Vec3(source.getX(), source.getY(), source.getZ());

                for (Entity currentEntity : list) {
                    if (!currentEntity.ignoreExplosion()) {
                        double currentEntityDistanceToExplosion = Mth.sqrt((float) currentEntity.distanceToSqr(explosionPos)) / radiusx2;
                        if (currentEntityDistanceToExplosion <= 1.0D) {
                            double currentEntityDistanceX = currentEntity.getX() - source.getX();
                            double currentEntityDistanceY = (currentEntity instanceof PrimedTnt ? currentEntity.getY() : currentEntity.getEyeY()) - source.getY();
                            double currentEntityDistanceZ = currentEntity.getZ() - source.getZ();
                            double pythagoreanDistance = Mth.sqrt((float) (currentEntityDistanceX * currentEntityDistanceX + currentEntityDistanceY * currentEntityDistanceY + currentEntityDistanceZ * currentEntityDistanceZ));
                            if (pythagoreanDistance != 0.0D) {
                                currentEntityDistanceX = currentEntityDistanceX / pythagoreanDistance;
                                currentEntityDistanceY = currentEntityDistanceY / pythagoreanDistance;
                                currentEntityDistanceZ = currentEntityDistanceZ / pythagoreanDistance;
                                double rayLength = Explosion.getSeenPercent(explosionPos, currentEntity);
                                double damageFalloff = (1.0D - currentEntityDistanceToExplosion) * rayLength;
                                currentEntity.hurt(this.getDamageSource(), (float) ((int) ((damageFalloff * damageFalloff + damageFalloff) / 2.0D * 7.0D * (double) radiusx2 + 1.0D)));
                                if (currentEntity instanceof LivingEntity) {
                                    damageFalloff = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity) currentEntity, damageFalloff);
                                }

                                currentEntity.setDeltaMovement(currentEntity.getDeltaMovement().add(currentEntityDistanceX * damageFalloff, currentEntityDistanceY * damageFalloff, currentEntityDistanceZ * damageFalloff));
                                if (currentEntity instanceof Player) {
                                    Player playerentity = (Player) currentEntity;
                                    if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.getAbilities().flying)) {
                                        meta.entityDisplacements.put(playerentity, new Vec3(currentEntityDistanceX * damageFalloff, currentEntityDistanceY * damageFalloff, currentEntityDistanceZ * damageFalloff));
                                    }
                                }
                            }
                        }
                    }
                }
                return meta;
            case STAGE_BLOCKS:
                if (worldIn.isClientSide) {
                    worldIn.playLocalSound(source.getX(), source.getY(), source.getZ(), properties.getExplosionSound(), SoundSource.BLOCKS, 4.0F, (1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.2F) * 0.7F, false);
                }

                if (properties.doesMakeParticles()) {
                    worldIn.addParticle(properties.getParticleToEmit(), source.getX(), source.getY(), source.getZ(), 1.0D, 0.0D, 0.0D);
                }

                if (doBlocksDrop) {
                    ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
                    Util.shuffle(meta.affectedBlocks, worldIn.random);

                    for(BlockPos blockpos : meta.affectedBlocks) {
                        BlockState blockstate = worldIn.getBlockState(blockpos);
                        Block block = blockstate.getBlock();

                        Explosion vanillaExplosion2 = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radius, meta.affectedBlocks);

                        if (!blockstate.isAir()) {
                            BlockPos blockpos1 = blockpos.immutable();
                            worldIn.getProfiler().push("explosion_blocks");
                            if (blockstate.canDropFromExplosion(worldIn, blockpos, vanillaExplosion2)) {
                                Level level = worldIn;
                                if (level instanceof ServerLevel serverlevel) {
                                    BlockEntity blockentity = blockstate.hasBlockEntity() ? worldIn.getBlockEntity(blockpos) : null;
                                    LootParams.Builder lootparams$builder = (new LootParams.Builder(serverlevel)).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockentity).withOptionalParameter(LootContextParams.THIS_ENTITY, placer);
                                    lootparams$builder.withParameter(LootContextParams.EXPLOSION_RADIUS, (float) this.radius);

                                    boolean flag1 = placer instanceof Player;
                                    blockstate.spawnAfterBreak(serverlevel, blockpos, ItemStack.EMPTY, flag1);

                                    blockstate.getDrops(lootparams$builder).forEach((stack) -> handleExplosionDrops(objectarraylist, stack, blockpos1));
                                }
                            }

                            blockstate.onBlockExploded(worldIn, blockpos, vanillaExplosion2);
                            worldIn.getProfiler().pop();
                        }
                    }

                    for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                        if(!worldIn.isClientSide)
                            Block.popResource(worldIn, pair.getSecond(), pair.getFirst());
                    }
                }

                if (properties.causesFire()) {
                    for (BlockPos blockpos2 : meta.affectedBlocks) {
                        if (worldIn.random.nextInt(3) == 0 && worldIn.getBlockState(blockpos2).isAir() && worldIn.getBlockState(blockpos2.below()).isSolidRender(worldIn, blockpos2.below())) {
                            if(!worldIn.isClientSide)
                                worldIn.setBlockAndUpdate(blockpos2, BaseFireBlock.getState(worldIn, blockpos2));
                        }
                    }
                }

                return meta;

            case STAGE_SYNC:
                // We have to manually tell the client that these blocks have broken, as onBlockExploded does not.
                if(!worldIn.isClientSide) {
                    ServerLevel sWorld = (ServerLevel) worldIn;
                    for (ServerPlayer serverplayerentity : sWorld.players()) {
                        if (serverplayerentity.distanceToSqr(source.getX(), source.getY(), source.getZ()) < 4096.0D) {
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
