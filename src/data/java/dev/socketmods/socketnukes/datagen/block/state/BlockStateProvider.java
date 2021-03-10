package dev.socketmods.socketnukes.datagen.block.state;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.datagen.block.model.BlockModelProviders;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, SocketNukes.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(SNRegistry.GENERIC_EXPLOSIVE.get(), BlockModelProviders.explosiveModel);
    }
}
