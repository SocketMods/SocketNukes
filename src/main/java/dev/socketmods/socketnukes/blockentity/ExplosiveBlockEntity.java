package dev.socketmods.socketnukes.blockentity;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExplosiveBlockEntity extends BlockEntity {
    private ResourceLocation configuration;

    public ExplosiveBlockEntity(BlockPos pos, BlockState state) {
        super(SNRegistry.EXPLOSIVE_TE.get(), pos, state);
        configuration = SNRegistry.EXPLOSION_TYPE_REGISTRY.get().getKey(SNRegistry.NULL_EXPLOSION.get());
    }


    public ResourceLocation getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ResourceLocation configuration) {
        this.configuration = configuration;
    }
}