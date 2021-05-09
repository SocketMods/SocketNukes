package dev.socketmods.socketnukes.actors.example;

import dev.socketmods.socketnukes.actors.Role;
import dev.socketmods.socketnukes.actors.common.BlockPosActor;
import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

public class BolbSpawnActor extends BlockPosActor {

    private static final int BOLB_MAX_SIZE = 1;

    private final int count;
    private final int cooldown;
    private int triggers;
    private int timer;

    public BolbSpawnActor(BlockPos position, int count, int triggers, int cooldown) {
        this(position, count, triggers, cooldown, cooldown);
    }

    public BolbSpawnActor(BlockPos position, int count, int triggers, int cooldown, int timer) {
        super(position);
        this.count = count;
        this.cooldown = cooldown;
        this.triggers = triggers;
        this.timer = timer;
    }

    @Override
    public boolean tick(ServerWorld world) {
        // If timer has finished fire explosion
        if (timer == 0) {
            explode(world);
            timer = cooldown;
            triggers--;
        }

        // We want to die if we have no work left to do
        if (triggers == 0) return true;

        // Otherwise reduce the timer
        timer--;

        // Spawn Particles
        world.spawnParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, position.getX(), position.getY(), position.getZ(), 1, 0.5D, 0.5D, 0.5D, 0.5D);

        return false;
    }

    private void explode(ServerWorld world) {
        for (int i = 0; i < count; i++) {
            BolbEntity e = new BolbEntity(SNRegistry.BOLB_ENTITY_TYPE.get(), world);
            e.setPosition(position.getX(), position.getY() + 1, position.getZ());
            e.setSlimeSize(world.rand.nextInt(BOLB_MAX_SIZE) + 1, true);
            e.velocityChanged = true;
            world.addEntity(e);
        }

        world.playSound(null, position, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
    }

    @Override
    public Role<?> getRole() {
        return SNRegistry.BOLB_SPAWN_ROLE.get();
    }

    @Override
    public CompoundNBT serialize(CompoundNBT nbt) {
        super.serialize(nbt);

        nbt.putInt("count", count);
        nbt.putInt("triggers", triggers);
        nbt.putInt("cooldown", cooldown);
        nbt.putInt("timer", timer);

        return nbt;
    }

}

