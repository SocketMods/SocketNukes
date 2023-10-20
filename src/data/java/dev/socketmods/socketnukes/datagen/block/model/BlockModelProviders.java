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
    public static ModelFile poweredFurnaceModel;

    public BlockModelProviders(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        String vanillaExplosive = ForgeRegistries.BLOCKS.getKey(SNRegistry.GENERIC_EXPLOSIVE.get()).getPath();
        explosiveModel = cubeBottomTop(vanillaExplosive, modLoc("block/" + vanillaExplosive + "_side"), modLoc("block/" + vanillaExplosive + "_bottom"), modLoc("block/" + vanillaExplosive + "_top"));

        String poweredFurnace = ForgeRegistries.BLOCKS.getKey(SNRegistry.POWERED_FURNACE_BLOCK.get()).getPath();
        poweredFurnaceModel = cubeBottomTop(poweredFurnace, modLoc("block/" + poweredFurnace + "_side"), modLoc("block/" + poweredFurnace + "_bottom"), modLoc("block/" + poweredFurnace + "_top"));
    }

}
