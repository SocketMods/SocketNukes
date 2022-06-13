package dev.socketmods.socketnukes.explosion.meta;


import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains metadata about the explosion that is needed to be curried around the explode function.
 * Things like the list of blocks, list of entities and their displacements, etc.
 * Can be extended to provide meta exclusive to ExtendedExplosionTypes.
 */
public class ExplosionMetaPackage {

    public ObjectArrayList<BlockPos> affectedBlocks;
    public Map<Entity, Vec3> entityDisplacements;

    public ExplosionMetaPackage() {
        this.affectedBlocks = new ObjectArrayList<>();
        this.entityDisplacements = new HashMap<>();
    }
}
