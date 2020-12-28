package dev.socketmods.socketnukes.datagen.blockmodel;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProviders extends BlockModelProvider {

    public BlockModelProviders(DataGenerator generatorIn, ExistingFileHelper existingFileHelper) {
        super(generatorIn, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
