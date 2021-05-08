package dev.socketmods.socketnukes.explosion.types;

import java.util.Collections;

import dev.socketmods.socketnukes.actors.StageManager;
import dev.socketmods.socketnukes.actors.example.BolbSpawnActor;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Spawns 16 (configurable by modifying the int in the explode function) Bolb entities. They fly off in all directions.
 *
 * @author Citrine
 */
public class MultiStageBolbExplosionType extends ExtendedExplosionType {
    private static final int STAGE_POP = 1;

    public MultiStageBolbExplosionType(ExplosionProperties properties) {
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
            StageManager.get(worldIn).ifPresent(it -> {
                // We want 4 Bolbs 4 times with a gap of 120 ticks
                it.add(new BolbSpawnActor(source, 4, 4, 120));
            });
        }

        return meta;
    }
}
