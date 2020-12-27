package dev.socketmods.socketnukes.datagen.blockmodel;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProviders extends BlockModelProvider {

    public BlockModelProviders(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
