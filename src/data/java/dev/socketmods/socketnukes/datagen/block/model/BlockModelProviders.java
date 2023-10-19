package dev.socketmods.socketnukes.datagen.block.model;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockModelProviders extends BlockModelProvider {
    public static ModelFile explosiveModel;

    public BlockModelProviders(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        String vanillaExplosive = ForgeRegistries.BLOCKS.getKey(SNRegistry.GENERIC_EXPLOSIVE.get()).getPath();
        explosiveModel = cubeBottomTop(vanillaExplosive, modLoc("blocks/" + vanillaExplosive + "_side"), modLoc("blocks/" + vanillaExplosive + "_bottom"), modLoc("blocks/" + vanillaExplosive + "_top"));
    }

}
