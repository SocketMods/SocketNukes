package dev.socketmods.socketnukes.datagen.tags;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class FluidTagsProviders extends FluidTagsProvider {

    public FluidTagsProviders(GatherDataEvent event) {
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
