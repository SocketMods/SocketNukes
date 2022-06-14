package dev.socketmods.socketnukes.datagen.itemmodel;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModelProviders extends ItemModelProvider {

    public ItemModelProviders(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Exploder
        String exploderResource = itemPath(SNRegistry.EXPLODER_ITEM.get());
        //generatedModels.put(exploderResource, factory.apply(exploderResource).texture("layer0", exploderResource));
        singleTexture("item/" + exploderResource,       // destination
                mcLoc("item/generated"),                // "parent": ###
                "layer0",                            // ###: TEXTURE
                modLoc("items/" + exploderResource));   // LAYER: ###


        cubeBottomTop(SNRegistry.POWERED_FURNACE_BLOCK.get());
        cubeBottomTop(SNRegistry.GENERIC_EXPLOSIVE.get());
    }

    /**
     * Constructs a cubeBottomTop with the given Block.
     * Textures are "${block.registryKey.path}_side", .._bottom and .._top
     */
    private ItemModelBuilder cubeBottomTop(Block block) {
        String path = blockPath(block);

        return cubeBottomTop(path, sideModel(path), bottomModel(path), topModel(path));
    }

    private String blockPath(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    private String itemPath(Item item) {
        return ForgeRegistries.ITEMS.getKey(item).getPath();
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
