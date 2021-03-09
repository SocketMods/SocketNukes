package dev.socketmods.socketnukes.explosion;

import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for the ExtendedExplosions, intended to be used where an Explosion is hard coded as a requirement,
 * for example the Forge events, and calculating resistances.
 *
 * It is what actually executes the ExtendedExplosionType#explode method, for every stage.
 *
 * @author Citrine
 */
public class DummyExplosion extends Explosion {

    private ExtendedExplosionType explosionType;

    protected World world;
    protected Entity source;
    protected BlockPos position;

    protected float radius;

    public DummyExplosion(World worldIn, @Nullable Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        super(worldIn, entityIn, x, y, z, size, affectedPositions);
        this.world = worldIn;
        this.source = entityIn;
        this.position = new BlockPos(x, y, z);
        this.radius = size;
    }

    public DummyExplosion(World worldIn, @Nullable Entity entityIn, double x, double y, double z, ExtendedExplosionType type) {
        super(worldIn, entityIn, x, y, z, type.getRadius(), new ArrayList<>());
        this.world = worldIn;
        this.source = entityIn;
        this.position = new BlockPos(x, y, z);
        this.radius = type.getRadius();
        explosionType = type;
    }

    // Execute the explosion extensibly.
    // TODO: tick delay?
    public void runExplosion() {
        if(!explosionType.prepareExplosion(world, position, source)) return;

        ExplosionMetaPackage meta = new ExplosionMetaPackage();

        for (int stage = 0; stage < explosionType.getExplosionStages(); stage++) {
            meta = explosionType.explode(world, position, stage + 1, source, meta);
        }

    }

    @Override
    public void doExplosionA() {
        // DO NOT RUN FOR CUSTOM EXPLOSIONS
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        // DO NOT RUN FOR CUSTOM EXPLOSIONS
    }
}
