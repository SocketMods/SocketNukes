package dev.socketmods.socketnukes.registry;


import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
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
    protected List<Block> immuneBlocks;
    public List<Block> getImmuneBlocks() { return immuneBlocks; }
    public void setImmuneBlocks(List<Block> blocks) { this.immuneBlocks = blocks; }

    // For conventional circular-crater explosions, this is the distance from the explosive to the outer edge of the crater.
    protected int radius;
    public int getRadius() { return radius; }
    public void setRadius(int radius) { this.radius = radius; }

    // For explosives that don't use conventional "big boom" damage, they can specify their own.
    // eg. napalm bombs cause fire damage
    protected DamageSource damageSource;
    public DamageSource getDamageSource() { return damageSource; }
    public void setDamageSource(DamageSource source) { this.damageSource = source; }

    // For explosives that totally annihilate the area, return false.
    // Analogous to Explosion.Mode.BREAK/DESTROY
    protected boolean doBlocksDrop;
    public boolean getDoBlocksDrop() { return doBlocksDrop; }
    public void setDoBlocksDrop(boolean newBlocksDrop) { this.doBlocksDrop = newBlocksDrop; }

    // Large explosions may require multiple "stages" of explosion, in order to avoid thrashing the server.
    protected int explosionStages;
    public int getExplosionStages() { return explosionStages; }
    public void setExplosionStages(int stages) { this.explosionStages = stages; }

    // Other properties about the explosion itself, the sound it makes, particles, etc.
    protected ExplosionProperties properties;
    public void setProperties(ExplosionProperties properties) { this.properties = properties; }
    public ExplosionProperties getProperties() { return properties; }

    public ExtendedExplosionType(int blastRadius, List<Block> immuneBlocks, int stages, DamageSource damage, boolean annihilateMode) {
        this.immuneBlocks = immuneBlocks;
        this.radius = blastRadius;
        this.damageSource = damage;
        this.doBlocksDrop = !annihilateMode;
        this.explosionStages = stages;
    }

    /**
     * Given some information about the state of the explosive,
     * how does it prepare to explode?
     *
     * @return boolean; whether to continue with the explosion
     */

    public boolean prepareExplosion(World worldIn, BlockPos source, Entity placer) {
        return true;
    }

    /**
     * If prepareExplosion returned true, execute the stages of explosion.
     * We return a list of blockstates for compatibility with the vanilla explosion (for now), as it returns the list
     *  on the first pass, and uses it on the second.
     *
     * TODO: split this out into ExplosionMetaPackage to make it reusable for every Explosion?
     */
    public List<BlockPos> explode(World worldIn, BlockPos source, int stage, Entity placer, List<BlockPos> blocksFromLastState) {
        return new ArrayList<>();
    }

    /**
     * Some explosions may be neutered - Red Matter via Black Hole.
     * This allows the explosion to handle these interactions.
     */

    public void stopExploding() {

    }


}