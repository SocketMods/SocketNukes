package dev.socketmods.socketnukes.explosion.types;

import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import net.minecraft.world.damagesource.DamageSource;

import java.util.ArrayList;


/**
 * This Explosion Type does nothing.
 * Handy for resolving conflicts - ie. when an ExplosiveEntity doesn't sync correctly to the client.
 */

public class NullExplosionType extends ExtendedExplosionType {

    public NullExplosionType() {
        super(0, new ArrayList<>(), 0, false);
        this.setFuseTime(10);
    }
}
