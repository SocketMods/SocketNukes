package dev.socketmods.socketnukes.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class DummyExplosion extends Explosion {

    public DummyExplosion(World worldIn, @Nullable Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions) {
        super(worldIn, entityIn, x, y, z, size, affectedPositions);
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
