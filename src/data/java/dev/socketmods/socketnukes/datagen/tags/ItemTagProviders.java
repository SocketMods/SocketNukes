package dev.socketmods.socketnukes.datagen.tags;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagProviders extends ItemTagsProvider {

    public ItemTagProviders(DataGenerator generatorIn, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, blockTagProvider, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {

    }
}
