package dev.socketmods.socketnukes.datagen.tags;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class FluidTagsProviders extends FluidTagsProvider {

    public FluidTagsProviders(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, SocketNukes.MODID, existingFileHelper);
    }
}
