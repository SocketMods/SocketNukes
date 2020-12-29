package dev.socketmods.socketnukes.registry;


import net.minecraft.block.Block;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

/**
 * Extended-capability Explosion
 *
 * The vanilla Explosion system is too hard-coded and limited for our needs,
 *  so this is a reimplementation with less limiting functionality
 *
 * It exists as a ForgeRegistryEntry so that it may be instantiated by the mod
 *  and addons, to ensure cross-functionality.
 *
 * The goal is that these shall be exposed to JSON files, for data-driven bombs.
 */

public class ExtendedExplosionType extends ForgeRegistryEntry<ExtendedExplosionType> {
    /**
     * These Explosion types are singleton - Registry Entries
     * Thus, they need to encode core behavior WITHOUT being instance specific.
     */

    // Blocks that are known to be immune to this explosion
    private List<Block> immuneBlocks;

    // For conventional circular-crater explosions, this is the distance from the explosive to the outer edge of the crater.
    private int radius;

    // For explosives that don't use conventional "big boom" damage, they can specify their own.
    // eg. napalm bombs cause fire damage
    private DamageSource damageSource;

    // For explosives that totally annihilate the area, return false.
    // Analogous to Explosion.Mode.BREAK/DESTROY
    private boolean doBlocksDrop;

    // Large explosions may require multiple "stages" of explosion, in order to avoid thrashing the server.
    private int explosionStages;

    public ExtendedExplosionType(int blastRadius, List<Block> immuneBlocks, int stages, DamageSource damage, boolean annihilateMode) {
        this.immuneBlocks = immuneBlocks;
        this.radius = blastRadius;
        this.damageSource = damage;
        this.doBlocksDrop = annihilateMode;
        this.explosionStages = stages;
    }

    /**
     * Given some information about the state of the explosive,
     * how does it prepare to explode?
     *
     * @return boolean; whether to continue with the explosion
     */

    private boolean prepareExplosion(World worldIn, BlockPos source) {

        return true;
    }

    /**
     * If prepareExplosion returned true, execute the stages of explosion.
     */

    private void explode(World worldIn, BlockPos source, int stage) {
    }

    /**
     * Some explosions may be neutered - Red Matter via Black Hole.
     * This allows the explosion to handle these interactions.
     */

    private void stopExploding() {

    }


}