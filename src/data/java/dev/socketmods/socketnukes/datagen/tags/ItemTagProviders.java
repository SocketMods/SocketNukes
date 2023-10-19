package dev.socketmods.socketnukes.datagen.tags;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ItemTagProviders extends ItemTagsProvider {

    public ItemTagProviders(GatherDataEvent event, BlockTagsProvider blockTagProvider) {
        super(event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                blockTagProvider.contentsGetter(),
                SocketNukes.MODID,
                event.getExistingFileHelper());
        //super(generatorIn, blockTagProvider, SocketNukes.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
