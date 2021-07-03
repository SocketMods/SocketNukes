package dev.socketmods.socketnukes.explosion.types;

import com.mojang.datafixers.util.Pair;
import dev.socketmods.socketnukes.explosion.DummyExplosion;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import dev.socketmods.socketnukes.networking.Network;
import dev.socketmods.socketnukes.networking.packet.ExtendedExplosionPacket;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Creates a perfect cube explosion, removing everything except obsidian and bedrock.
 * Serves as the last example explosion type, to demonstrate the extensibility of the system.
 *
 * @author Citrine
 */
public class CubicExplosionType extends ExtendedExplosionType {
    private final ExplosionProperties properties;

    // Stage constants - we gather the blocks to be broken in stage 1, and actually break them in stage 2.
    private static final int STAGE_PREPARE = 1;
    private static final int STAGE_BREAK = 2;

    public CubicExplosionType(ExplosionProperties props) {
        super(4, Arrays.asList(Blocks.OBSIDIAN, Blocks.BEDROCK), 2, DamageSource.GENERIC, false);
        this.properties = props;
        this.setFuseTime(80);
    }

    @Override
    public ExplosionMetaPackage explode(World worldIn, BlockPos source, int stage, Entity placer, ExplosionMetaPackage meta) {
        switch(stage) {
            case STAGE_PREPARE:
                for (int xPos = source.getX() - radius; xPos < source.getX() + radius; xPos++) {
                    for (int yPos = source.getY() - radius; yPos < source.getY() + radius; yPos++) {
                        for (int zPos = source.getZ() - radius; zPos < source.getZ() + radius; zPos++) {
                            BlockPos pos = new BlockPos(xPos, yPos, zPos);
                            if(!immuneBlocks.contains(worldIn.getBlockState(pos).getBlock()))
                                meta.affectedBlocks.add(new BlockPos(xPos, yPos, zPos));
                        }
                    }
                }

                return meta;
            case STAGE_BREAK:
                // Entity damage range is quite a bit bigger than the block breaking range.
                float radiusx2 = radius * 2.0F;
                int eastBound = MathHelper.floor(source.getX() - (double) radiusx2 - 1.0D);
                int westBound = MathHelper.floor(source.getX() + (double) radiusx2 + 1.0D);
                int lowerBound = MathHelper.floor(source.getY() - (double) radiusx2 - 1.0D);
                int upperBound = MathHelper.floor(source.getY() + (double) radiusx2 + 1.0D);
                int southBound = MathHelper.floor(source.getZ() - (double) radiusx2 - 1.0D);
                int northBound = MathHelper.floor(source.getZ() + (double) radiusx2 + 1.0D);

                List<Entity> list = worldIn.getEntities(placer, new AxisAlignedBB(eastBound, lowerBound, southBound, westBound, upperBound, northBound));

                DummyExplosion vanillaExplosion = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radiusx2, meta.affectedBlocks);
                ForgeEventFactory.onExplosionDetonate(worldIn, vanillaExplosion, list, radiusx2);
                Vector3d explosionPos = new Vector3d(source.getX(), source.getY(), source.getZ());

                for (Entity currentEntity : list) {
                    if (!currentEntity.ignoreExplosion()) {
                        double currentEntityDistanceToExplosion = MathHelper.sqrt(currentEntity.distanceToSqr(explosionPos)) / radiusx2;
                        if (currentEntityDistanceToExplosion <= 1.0D) {
                            double currentEntityDistanceX = currentEntity.getX() - source.getX();
                            double currentEntityDistanceY = (currentEntity instanceof TNTEntity ? currentEntity.getY() : currentEntity.getEyeY()) - source.getY();
                            double currentEntityDistanceZ = currentEntity.getZ() - source.getZ();
                            double pythagoreanDistance = MathHelper.sqrt(currentEntityDistanceX * currentEntityDistanceX + currentEntityDistanceY * currentEntityDistanceY + currentEntityDistanceZ * currentEntityDistanceZ);
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
                                if (currentEntity instanceof PlayerEntity) {
                                    PlayerEntity playerentity = (PlayerEntity) currentEntity;
                                    if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.flying)) {
                                        meta.entityDisplacements.put(playerentity, new Vector3d(currentEntityDistanceX * damageFalloff, currentEntityDistanceY * damageFalloff, currentEntityDistanceZ * damageFalloff));
                                    }
                                }
                            }
                        }
                    }
                }

                if (worldIn.isClientSide) {
                    worldIn.playLocalSound(source.getX(), source.getY(), source.getZ(), properties.getExplosionSound(), SoundCategory.BLOCKS, 4.0F, (1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.2F) * 0.7F, false);
                }

                if (properties.doesMakeParticles()) {
                    worldIn.addParticle(properties.getParticleToEmit(), source.getX(), source.getY(), source.getZ(), 1.0D, 0.0D, 0.0D);
                }

                if (doBlocksDrop) {
                    ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
                    Collections.shuffle(meta.affectedBlocks, worldIn.random);

                    for (BlockPos blockpos : meta.affectedBlocks) {
                        BlockState blockstate = worldIn.getBlockState(blockpos);
                        if (!blockstate.isAir(worldIn, blockpos)) {
                            BlockPos blockpos1 = blockpos.immutable();
                            worldIn.getProfiler().push("explosion_blocks");

                            Explosion vanillaExplosion2 = new DummyExplosion(worldIn, placer, source.getX(), source.getY(), source.getZ(), radius, meta.affectedBlocks);

                            if (blockstate.canDropFromExplosion(worldIn, blockpos, vanillaExplosion2) && worldIn instanceof ServerWorld) {
                                TileEntity tileentity = blockstate.hasTileEntity() ? worldIn.getBlockEntity(blockpos) : null;
                                LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld) worldIn)).withRandom(worldIn.random).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(blockpos)).withParameter(LootParameters.TOOL, ItemStack.EMPTY).withOptionalParameter(LootParameters.BLOCK_ENTITY, tileentity).withOptionalParameter(LootParameters.THIS_ENTITY, placer);
                                if (this.doBlocksDrop) {
                                    lootcontext$builder.withParameter(LootParameters.EXPLOSION_RADIUS, (float) radius);
                                }

                                blockstate.getDrops(lootcontext$builder).forEach((stack) -> handleExplosionDrops(objectarraylist, stack, blockpos1));
                            }
                            if(!worldIn.isClientSide)
                                blockstate.onBlockExploded(worldIn, blockpos, vanillaExplosion2);
                            worldIn.getProfiler().pop();
                        }
                    }

                    for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                        if(!worldIn.isClientSide)
                            Block.popResource(worldIn, pair.getSecond(), pair.getFirst());
                    }
                }

                // We have to manually tell the client that these blocks have broken, as onBlockExploded does not.
                if(!worldIn.isClientSide) {
                    ServerWorld sWorld = (ServerWorld) worldIn;
                    for (ServerPlayerEntity serverplayerentity : sWorld.players()) {
                        if (serverplayerentity.distanceToSqr(source.getX(), source.getY(), source.getZ()) < 4096.0D) {
                            Network.sendToClient(new ExtendedExplosionPacket(source, meta.affectedBlocks, properties.getParticleToEmit().getType(), properties.getExplosionSound(), meta.entityDisplacements), serverplayerentity);
                        }
                    }
                }

                return meta;
            default:
                return meta;
        }
    }
}
