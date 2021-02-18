package dev.socketmods.socketnukes.entity;

import dev.socketmods.socketnukes.explosion.DummyExplosion;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class ExplosiveEntity extends Entity {
    private static final DataParameter<Integer> FUSE = EntityDataManager.createKey(ExplosiveEntity.class, DataSerializers.VARINT);
    @Nullable
    private LivingEntity placer;
    private int fuse = 80;

    private ExtendedExplosionType explosion;

    public ExplosiveEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public ExplosiveEntity(World worldIn, BlockPos position, ExtendedExplosionType explosive, @Nullable LivingEntity igniter) {
        this(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), worldIn);
        this.setPosition(position.getX(), position.getY(), position.getZ());
        double d0 = worldIn.rand.nextDouble() * (double)((float)Math.PI * 2F);
        this.setMotion(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.prevPosX = position.getX();
        this.prevPosY = position.getY();
        this.prevPosZ = position.getZ();
        this.placer = igniter;
        this.explosion = explosive;
    }

    public static ExplosiveEntity create(EntityType<?> type, World worldin) {
        return new ExplosiveEntity(type, worldin);
    }

    @Override
    public void tick() {
        if (!this.hasNoGravity()) {
            this.setMotion(this.getMotion().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getMotion());
        this.setMotion(this.getMotion().scale(0.98D));
        if (this.onGround) {
            this.setMotion(this.getMotion().mul(0.7D, -0.5D, 0.7D));
        }

        --this.fuse;
        if (this.fuse <= 0) {
            this.remove();
            if (!this.world.isRemote) {
                this.explode();
            }
        } else {
            this.func_233566_aG_();
            if (this.world.isRemote) {
                this.world.addParticle(ParticleTypes.SMOKE, this.getPosX(), this.getPosY() + 0.5D, this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void explode() {
        DummyExplosion explosion = new DummyExplosion(this.world, placer,
            this.getPosX(), this.getPosY() - 1, this.getPosZ(),
            this.explosion);

        explosion.runExplosion();
    }

    @Override
    protected void registerData() {
        this.dataManager.register(FUSE, 80);
    }

    protected void writeAdditional(CompoundNBT compound) {
        compound.putShort("Fuse", (short)this.getFuse());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(CompoundNBT compound) {
        this.setFuse(compound.getShort("Fuse"));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setFuse(int fuseIn) {
        this.dataManager.set(FUSE, fuseIn);
        this.fuse = fuseIn;
    }
    public int getFuse() {
        return this.fuse;
    }
}
