package dev.socketmods.socketnukes.datagen.block.model;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockModelProviders extends BlockModelProvider {
    public static ModelFile explosiveModel;
    public static ModelFile poweredFurnaceModel;

    public BlockModelProviders(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        String vanillaExplosive = path(SNRegistry.GENERIC_EXPLOSIVE.get());
        String poweredFurnace = path(SNRegistry.POWERED_FURNACE.get());

        explosiveModel = cubeBottomTop(vanillaExplosive, sideModel(vanillaExplosive), bottomModel(vanillaExplosive), topModel(vanillaExplosive));
        poweredFurnaceModel = cubeBottomTop(poweredFurnace, sideModel(poweredFurnace), bottomModel(poweredFurnace), topModel(poweredFurnace));
    }

    private String path(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    private ResourceLocation sideModel(String block) {
        return modLoc("blocks/" + block + "_side");
    }

    private ResourceLocation bottomModel(String block) {
        return modLoc("blocks/" + block + "_bottom");
    }

    private ResourceLocation topModel(String block) {
        return modLoc("blocks/" + block + "_top");
    }

}
