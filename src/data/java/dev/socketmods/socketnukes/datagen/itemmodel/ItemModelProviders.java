package dev.socketmods.socketnukes.datagen.itemmodel;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.PackOutput;
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
        String exploderResource = ForgeRegistries.ITEMS.getKey(SNRegistry.EXPLODER_ITEM.get()).getPath(); // getPath() is nullable
        //generatedModels.put(exploderResource, factory.apply(exploderResource).texture("layer0", exploderResource));
        singleTexture("item/" + exploderResource,       // destination
                mcLoc("item/generated"),                // "parent": ###
                "layer0",                            // ###: TEXTURE
                modLoc("items/" + exploderResource));   // LAYER: ###

        String vanillaExplosive = ForgeRegistries.BLOCKS.getKey(SNRegistry.GENERIC_EXPLOSIVE.get()).getPath();
        cubeBottomTop(vanillaExplosive, modLoc("blocks/" + vanillaExplosive + "_side"), modLoc("blocks/" + vanillaExplosive + "_bottom"), modLoc("blocks/" + vanillaExplosive + "_top"));

    }
}
