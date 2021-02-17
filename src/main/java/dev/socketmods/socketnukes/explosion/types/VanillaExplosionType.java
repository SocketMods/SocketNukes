package dev.socketmods.socketnukes.explosion.types;

import com.google.common.collect.Sets;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.TNTEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.*;

public class VanillaExplosionType extends ExtendedExplosionType {

    public VanillaExplosionType() {
        super(4, Arrays.asList(Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN),
                2, DamageSource.GENERIC, false);
    }

    @Override
    public boolean prepareExplosion(World worldIn, BlockPos source) {

        return true;
    }

    @Override
    public void explode(World worldIn, BlockPos source, int stage) {

        switch(stage) {
            case 1:
                // Break blocks
                Set<BlockPos> set = Sets.newHashSet();
                int i = 16;

                for(int j = 0; j < 16; ++j) {
                    for(int k = 0; k < 16; ++k) {
                        for(int l = 0; l < 16; ++l) {
                            if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                                double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                                double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                                double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
                                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                d0 = d0 / d3;
                                d1 = d1 / d3;
                                d2 = d2 / d3;
                                float f = radius * (0.7F + worldIn.rand.nextFloat() * 0.6F);
                                double d4 = source.getX();
                                double d6 = source.getY();
                                double d8 = source.getZ();

                                for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                    BlockPos blockpos = new BlockPos(d4, d6, d8);
                                    BlockState blockstate = worldIn.getBlockState(blockpos);
                                    FluidState fluidstate = worldIn.getFluidState(blockpos);
                                    // this.context.getExplosionResistance(this, this.world, blockpos, blockstate, fluidstate);
                                    // We use the hardcoded Block#getExplosionResistance & FluidState.getExplosionResistance
                                    Optional<Float> optional = (blockstate != null && fluidstate != null) ? Optional.of(Math.max(blockstate.getBlock().getExplosionResistance(), fluidstate.getExplosionResistance())) : Optional.empty();
                                    if (optional.isPresent()) {
                                        f -= (optional.get() + 0.3F) * 0.3F;
                                    }

                                    if (f > 0.0F && !immuneBlocks.contains(worldIn.getBlockState(blockpos).getBlock())) {
                                        set.add(blockpos);
                                    }

                                    d4 += d0 * (double)0.3F;
                                    d6 += d1 * (double)0.3F;
                                    d8 += d2 * (double)0.3F;
                                }
                            }
                        }
                    }
                }

                this.affectedBlockPositions.addAll(set);
                float f2 = radius * 2.0F;
                int k1 = MathHelper.floor(source.getX() - (double)f2 - 1.0D);
                int l1 = MathHelper.floor(source.getX() + (double)f2 + 1.0D);
                int i2 = MathHelper.floor(source.getY() - (double)f2 - 1.0D);
                int i1 = MathHelper.floor(source.getY() + (double)f2 + 1.0D);
                int j2 = MathHelper.floor(source.getZ() - (double)f2 - 1.0D);
                int j1 = MathHelper.floor(source.getZ() + (double)f2 + 1.0D);
                List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(this.exploder, new AxisAlignedBB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
                net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.world, this, list, f2);
                Vector3d vector3d = new Vector3d(this.x, this.y, this.z);

                for(int k2 = 0; k2 < list.size(); ++k2) {
                    Entity entity = list.get(k2);
                    if (!entity.isImmuneToExplosions()) {
                        double d12 = (double)(MathHelper.sqrt(entity.getDistanceSq(vector3d)) / f2);
                        if (d12 <= 1.0D) {
                            double d5 = entity.getPosX() - this.x;
                            double d7 = (entity instanceof TNTEntity ? entity.getPosY() : entity.getPosYEye()) - this.y;
                            double d9 = entity.getPosZ() - this.z;
                            double d13 = (double)MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                            if (d13 != 0.0D) {
                                d5 = d5 / d13;
                                d7 = d7 / d13;
                                d9 = d9 / d13;
                                double d14 = (double)getBlockDensity(vector3d, entity);
                                double d10 = (1.0D - d12) * d14;
                                entity.attackEntityFrom(this.getDamageSource(), (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f2 + 1.0D)));
                                double d11 = d10;
                                if (entity instanceof LivingEntity) {
                                    d11 = ProtectionEnchantment.getBlastDamageReduction((LivingEntity)entity, d10);
                                }

                                entity.setMotion(entity.getMotion().add(d5 * d11, d7 * d11, d9 * d11));
                                if (entity instanceof PlayerEntity) {
                                    PlayerEntity playerentity = (PlayerEntity)entity;
                                    if (!playerentity.isSpectator() && (!playerentity.isCreative() || !playerentity.abilities.isFlying)) {
                                        this.playerKnockbackMap.put(playerentity, new Vector3d(d5 * d10, d7 * d10, d9 * d10));
                                    }
                                }
                            }
                        }
                    }
                }
            case 2:
                // Sounds & Damage
        }

    }
}
