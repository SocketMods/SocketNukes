package dev.socketmods.socketnukes.explosion.types;

import java.util.Collections;

import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Spawns 16 (configurable by modifying the int in the explode function) Bolb entities.
 * They fly off in all directions.
 *
 * @author Citrine
 */
public class BolbExplosionType extends ExtendedExplosionType {
    private static final int STAGE_POP = 1;
    private static final int BOLB_FACTOR = 16;
    private static final int BOLB_MAX_SIZE = 4;

    public BolbExplosionType(ExplosionProperties properties) {
        super(0, Collections.emptyList(), 1, DamageSource.GENERIC, false);
        this.properties = properties;
    }

    @Override
    public boolean prepareExplosion(World worldIn, BlockPos source, Entity placer) {
        return true;
    }

    @Override
    public ExplosionMetaPackage explode(World worldIn, BlockPos source, int stage, Entity placer, ExplosionMetaPackage meta) {
        if (stage == STAGE_POP) {

            // Spawn the entities, set the velocity, add to the world
            for (int i = 0; i < BOLB_FACTOR; i++) {
                double x = worldIn.rand.nextDouble() * 10;
                double y = worldIn.rand.nextDouble() * 10;
                double z = worldIn.rand.nextDouble() * 10;

                BolbEntity e = new BolbEntity(SNRegistry.BOLB_ENTITY_TYPE.get(), worldIn);
                e.setVelocity(x, y, z);
                e.setPosition(source.getX(), source.getY(), source.getZ());
                e.setSlimeSize(worldIn.rand.nextInt(BOLB_MAX_SIZE) + 1, true);
                e.velocityChanged = true;
                worldIn.addEntity(e);
            }

            // Spawn particle, play sound
            if (worldIn.isRemote) {
                worldIn.playSound(source.getX(), source.getY(), source.getZ(), properties.getExplosionSound(), SoundCategory.BLOCKS, 4.0F, (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F, false);
            }

            if (properties.doesMakeParticles()) {
                worldIn.addParticle(properties.getParticleToEmit(), source.getX(), source.getY(), source.getZ(), 1.0D, 0.0D, 0.0D);
            }
        }

        return meta;
    }
}
