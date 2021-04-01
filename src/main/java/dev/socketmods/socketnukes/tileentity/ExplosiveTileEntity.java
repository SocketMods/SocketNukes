package dev.socketmods.socketnukes.tileentity;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class ExplosiveTileEntity extends TileEntity {
    public ResourceLocation configuration;

    public ExplosiveTileEntity() {
        super(SNRegistry.EXPLOSIVE_TE.get());
        configuration = SNRegistry.NULL_EXPLOSION.get().getRegistryName();
    }


    public  ResourceLocation getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ResourceLocation configuration) {
        this.configuration = configuration;
    }
}
