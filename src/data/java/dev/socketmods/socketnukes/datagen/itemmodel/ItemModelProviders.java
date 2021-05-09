package dev.socketmods.socketnukes.datagen.itemmodel;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProviders extends ItemModelProvider {

    public ItemModelProviders(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Exploder
        String exploderResource = SNRegistry.getName(SNRegistry.EXPLODER_ITEM).getPath(); // getPath() is nullable
        //generatedModels.put(exploderResource, factory.apply(exploderResource).texture("layer0", exploderResource));
        singleTexture("item/" + exploderResource,       // destination
                mcLoc("item/generated"),                // "parent": ###
                "layer0",                            // ###: TEXTURE
                modLoc("items/" + exploderResource));   // LAYER: ###

        String vanillaExplosive = SNRegistry.getName(SNRegistry.GENERIC_EXPLOSIVE).getPath();
        cubeBottomTop(vanillaExplosive, modLoc("blocks/" + vanillaExplosive + "_side"), modLoc("blocks/" + vanillaExplosive + "_bottom"), modLoc("blocks/" + vanillaExplosive + "_top"));

    }
}
