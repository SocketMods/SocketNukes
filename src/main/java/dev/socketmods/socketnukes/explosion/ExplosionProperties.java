package dev.socketmods.socketnukes.explosion;

import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundEvent;

public class ExplosionProperties {
    private boolean makesParticles = false;
    private boolean causesFire = false;
    private IParticleData particleToEmit;

    private SoundEvent explosionSound;

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
