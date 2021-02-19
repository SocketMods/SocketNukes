package dev.socketmods.socketnukes.explosion;

import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DummyExplosion extends Explosion {

    private ExtendedExplosionType explosionType;

    protected World world;
    protected Entity source;
    protected BlockPos position;

    protected float radius;

    protected List<BlockPos> affectedPositions;

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

    public void runExplosion() {
        explosionType.prepareExplosion(world, position, source);

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
