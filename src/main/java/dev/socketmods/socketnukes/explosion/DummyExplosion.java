package dev.socketmods.socketnukes.explosion;

import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DummyExplosion extends Explosion {

    private ExtendedExplosionType explosionType;

    protected World world;
    protected Entity source;
    protected BlockPos position;

    public DummyExplosion(World worldIn, @Nullable Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        super(worldIn, entityIn, x, y, z, size, affectedPositions);
    }

    public DummyExplosion(World worldIn, @Nullable Entity entityIn, double x, double y, double z, ExtendedExplosionType type) {
        super(worldIn, entityIn, x, y, z, type.getRadius(), new ArrayList<>());
        this.world = worldIn;
        this.source = entityIn;
        this.position = new BlockPos(x, y, z);
        explosionType = type;
    }

    public void runExplosion() {
        explosionType.prepareExplosion(world, position, source);

        List<BlockPos> affectedBlocks = new ArrayList<>();

        for (int stage = 0; stage < explosionType.getExplosionStages(); stage++) {
            affectedBlocks = explosionType.explode(world, position, stage + 1, source, affectedBlocks);
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
