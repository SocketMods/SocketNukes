package dev.socketmods.socketnukes.registry;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(SocketNukes.MODID)
public class SNRegistry {

    /***********************************************
     *         Deferred Register Instances         *
     ***********************************************/

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SocketNukes.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SocketNukes.MODID);

    public static final DeferredRegister<TileEntityType<?>> TETYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SocketNukes.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERTYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SocketNukes.MODID);

    /***********************************************
     *          Registry Object Instances          *
     * Class is ObjectHolder, so make these names  *
     *         correspond to registry names.       *
     ***********************************************/




    public static void initialize() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        TETYPES.register(modBus);
        CONTAINERTYPES.register(modBus);
    }
}
