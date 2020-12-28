package dev.socketmods.socketnukes.datagen.itemmodel;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProviders extends ItemModelProvider {

    public ItemModelProviders(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
