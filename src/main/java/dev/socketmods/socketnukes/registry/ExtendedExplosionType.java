package dev.socketmods.socketnukes.registry;

import java.util.List;

import com.mojang.datafixers.util.Pair;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.meta.ExplosionMetaPackage;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

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
 *
 * @author Curle
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

    protected int fuseTime;
    public void setFuseTime(int newFuseTime) { this.fuseTime = newFuseTime; }
    public int getFuseTime() { return fuseTime; }

    public ExtendedExplosionType(int blastRadius, List<Block> immuneBlocks, int stages, DamageSource damage, boolean annihilateMode) {
        this.immuneBlocks = immuneBlocks;
        this.radius = blastRadius;
        this.damageSource = damage;
        this.doBlocksDrop = !annihilateMode;
        this.explosionStages = stages;
        this.fuseTime = 80;
    }

    /**
     * Given some information about the state of the explosive,
     * how does it prepare to explode?
     *
     * @return boolean; whether to continue with the explosion
     */

    public boolean prepareExplosion(Level worldIn, BlockPos source, Entity placer) {
        return true;
    }

    /**
     * If prepareExplosion returned true, execute the stages of explosion.
     *
     * We return a package of metadata that the explosion can carry around between stages.
     * The package is mutable, and it is intended to be changed by each progressive stage.
     *
     * It allows the stages to be executed independently, but using data from all prior.
     *
     */
    public ExplosionMetaPackage explode(Level worldIn, BlockPos source, int stage, Entity placer, ExplosionMetaPackage meta) {
        return new ExplosionMetaPackage();
    }

    /**
     * Some explosions may be neutered - Red Matter via Black Hole.
     * This allows the explosion to handle these interactions.
     */

    public void stopExploding() {

    }

    /**
     * Explosions that break blocks need logic for dropping blocks. Many multiple types of items that drop need to be merged into one.
     * This handles all the surrounding logic, so that it need not be duplicated in every explosion.
     */
    protected static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos) {
        int i = dropPositionArray.size();

        for(int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, stack)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, stack, 16);
                dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        dropPositionArray.add(Pair.of(stack, pos));
    }

    public TranslatableComponent getTranslationText() {
        ResourceLocation name = SNRegistry.getName(this);
        return new TranslatableComponent(name.getNamespace() + ".explosions." + name.getPath());
    }

}