package dev.socketmods.socketnukes.datagen.block.state;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.datagen.block.model.BlockModelProviders;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper, BlockModelProviders models) {
        super(output, SocketNukes.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(SNRegistry.GENERIC_EXPLOSIVE.get(), BlockModelProviders.explosiveModel);
        horizontalBlock(SNRegistry.POWERED_FURNACE_BLOCK.get(), BlockModelProviders.poweredFurnaceModel);
    }
}
