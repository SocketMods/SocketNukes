package dev.socketmods.socketnukes.datagen.itemmodel;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModelProviders extends ItemModelProvider {

    public ItemModelProviders(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Exploder
        generated(SNRegistry.EXPLODER_ITEM.get());
        handheld(SNRegistry.IRON_PLATE_ITEM.get());

        String vanillaExplosive = ForgeRegistries.BLOCKS.getKey(SNRegistry.GENERIC_EXPLOSIVE.get()).getPath();
        cubeBottomTop(vanillaExplosive, modLoc("block/" + vanillaExplosive + "_side"), modLoc("block/" + vanillaExplosive + "_bottom"), modLoc("block/" + vanillaExplosive + "_top"));

        String poweredFurnace =  ForgeRegistries.BLOCKS.getKey(SNRegistry.POWERED_FURNACE_BLOCK.get()).getPath();
        cubeBottomTop(poweredFurnace, modLoc("block/" + poweredFurnace + "_side"), modLoc("block/" + poweredFurnace + "_bottom"), modLoc("block/" + poweredFurnace + "_top"));
    }

    private void generated(Item item) {
        String resource = ForgeRegistries.ITEMS.getKey(item).getPath(); // getPath() is nullable
        //generatedModels.put(exploderResource, factory.apply(exploderResource).texture("layer0", exploderResource));
        singleTexture("item/" + resource,       // destination
                mcLoc("item/generated"),                // "parent": ###
                "layer0",                            // ###: TEXTURE
                modLoc("item/" + resource));   // LAYER: ###
    }

    private void handheld(Item item) {
        String resource = ForgeRegistries.ITEMS.getKey(item).getPath(); // getPath() is nullable
        //generatedModels.put(exploderResource, factory.apply(exploderResource).texture("layer0", exploderResource));
        singleTexture("item/" + resource,       // destination
                mcLoc("item/handheld"),                // "parent": ###
                "layer0",                            // ###: TEXTURE
                modLoc("item/" + resource));   // LAYER: ###
    }

}
