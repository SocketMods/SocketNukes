package dev.socketmods.socketnukes.explosion.types;

import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Spawns 16 (configurable by modifying the int in the explode function) Bolb entities.
 * They fly off in all directions.
 *
 * @author Citrine
 */
public class BolbExplosionType extends ExtendedExplosionType {
    private static final int STAGE_POP = 1;

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
            int entitiesToPop = 16;
            List<Vector3d> velocities = new ArrayList<>();

            // Generate 16 random velocities for them to appear at.
            // Every velocity here corresponds to the i-th entity to be spawned
            for (int i = 0; i < entitiesToPop; i++) {
                double x = worldIn.rand.nextDouble() * 10;
                double y = worldIn.rand.nextDouble() * 10;
                double z = worldIn.rand.nextDouble() * 10;
                velocities.add(new Vector3d(x, y, z));
            }

            // Spawn the entities, set the velocity, add to the world
            for (int i = 0; i < entitiesToPop; i++) {
                BolbEntity e = new BolbEntity(SNRegistry.EXPLOSIVE_BOLB_TYPE.get(), worldIn);
                e.setVelocity(velocities.get(i).x, velocities.get(i).y, velocities.get(i).z);
                e.setPosition(source.getX(), source.getY(), source.getZ());
                e.setSlimeSize(worldIn.rand.nextInt(4), true);
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
