package dev.socketmods.socketnukes.explosion.meta;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

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

    public List<BlockPos> affectedBlocks;
    public Map<Entity, Vector3d> entityDisplacements;

    public ExplosionMetaPackage() {
        this.affectedBlocks = new ArrayList<>();
        this.entityDisplacements = new HashMap<>();
    }
}
