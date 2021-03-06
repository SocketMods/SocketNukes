package dev.socketmods.socketnukes.explosion;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundEvent;

/**
 * Stores all the needed metadata for explosions, for carrying around information like the sound, particle,
 * whether it creates fire, etc etc.
 *
 * @author Citrine
 */
public class ExplosionProperties {
    private final boolean makesParticles;
    private final boolean causesFire;
    private final IParticleData particleToEmit;

    private final SoundEvent explosionSound;

    public ExplosionProperties(boolean particles, boolean fire, IParticleData particle, SoundEvent sound) {
        this.makesParticles = particles;
        this.causesFire = fire;
        this.particleToEmit = particle;
        this.explosionSound = sound;
    }

    public IParticleData getParticleToEmit() {
        return particleToEmit;
    }

    public SoundEvent getExplosionSound() {
        return explosionSound;
    }

    public boolean doesMakeParticles() {
        return makesParticles;
    }

    public boolean causesFire() {
        return causesFire;
    }
}
