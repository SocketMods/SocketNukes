package dev.socketmods.socketnukes.datagen.tags;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class BlockTagProviders extends BlockTagsProvider {

    public BlockTagProviders(GatherDataEvent event) {
        super(
                event.getGenerator().getPackOutput(),
                event.getLookupProvider(),
                SocketNukes.MODID,
                event.getExistingFileHelper()
        );
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

    }
}
