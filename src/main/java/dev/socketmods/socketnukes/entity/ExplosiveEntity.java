package dev.socketmods.socketnukes.entity;

import dev.socketmods.socketnukes.explosion.DummyExplosion;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

/**
 * The Explosive Entity is what runs the actual logic for exploding things from a TNTExplosive.
 * It waits, bouncing around slightly, before actually popping.
 * <p>
 * This entity can be given any ExplosiveType you need, and it will run that logic via DummyExplosion.
 *
 * @author Citrine
 */
public class ExplosiveEntity extends Entity {
    private static final EntityDataAccessor<Integer> FUSE = SynchedEntityData.defineId(ExplosiveEntity.class, EntityDataSerializers.INT);
    @Nullable
    private LivingEntity placer;
    private int fuse = 80;

    private ExtendedExplosionType explosion;

    public ExplosiveEntity(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public ExplosiveEntity(Level worldIn, BlockPos position, ExtendedExplosionType explosive, @Nullable LivingEntity igniter) {
        this(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), worldIn);
        this.setPos(position.getX(), position.getY(), position.getZ());
        double d0 = worldIn.random.nextDouble() * (double) ((float) Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xo = position.getX();
        this.yo = position.getY();
        this.zo = position.getZ();
        this.placer = igniter;
        this.explosion = explosive;
    }

    // Static factory method, used for registration.
    public static ExplosiveEntity create(EntityType<?> type, Level worldin) {
        return new ExplosiveEntity(type, worldin);
    }

    /**
     * Runs the actual logic for preparing to execute.
     * It slowly ticks down, bouncing slightly, creating smoke as a visual indicator.
     * Once the tick counter runs out, it explodes via the explode method.
     * <p>
     * When this Entity is synchronized from the server, it arrives with no extra data.
     * For this reason, when there is no metadata we fall back to a "do nothing" state.
     * This only runs when sync fails, which should be only from a TNTExplosive being ignited.
     * Nonetheless, this fix seems to work, and it seems to prevent nullpointers.
     */
    @Override
    public void tick() {
        if (explosion == null) {
            explosion = SNRegistry.NULL_EXPLOSION.get();
            return;
        }
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        --this.fuse;
        if (this.fuse <= 0) {
            this.remove(RemovalReason.DISCARDED);
            this.explode();
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    /**
     * Create the actual explosion, run it.
     * This is where all the work for abstraction comes together.
     */
    protected void explode() {
        DummyExplosion explosion = new DummyExplosion(this.level, placer,
                this.getX(), this.getY() - 1, this.getZ(),
                this.explosion);

        explosion.runExplosion();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(FUSE, 80);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putShort("fuse", (short) this.getFuse());
        compound.putString("explosionType", SNRegistry.getName(explosion).toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setFuse(compound.getShort("fuse"));
        String resLoc = compound.getString("explosionType");
        this.setExplosion(SNRegistry.getExplosionType(resLoc));
    }

    private void setExplosion(ExtendedExplosionType explosionType) {
        this.explosion = explosionType;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public int getFuse() {
        return this.fuse;
    }

    public void setFuse(int fuseIn) {
        this.entityData.set(FUSE, fuseIn);
        this.fuse = fuseIn;
    }
}
