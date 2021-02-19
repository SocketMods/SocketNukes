package dev.socketmods.socketnukes.datagen.block.model;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProviders extends BlockModelProvider {
    public static ModelFile explosiveModel;

    public BlockModelProviders(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        String vanillaExplosive = SNRegistry.GENERIC_EXPLOSIVE.get().getRegistryName().getPath();
        explosiveModel = cubeBottomTop(vanillaExplosive, modLoc("blocks/" + vanillaExplosive + "_side"), modLoc("blocks/" + vanillaExplosive + "_bottom"), modLoc("blocks/" + vanillaExplosive + "_top"));
    }

}
