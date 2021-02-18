package dev.socketmods.socketnukes.registry;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.types.VanillaExplosionType;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.item.util.ExploderItem;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class SNRegistry {

    /***********************************************
     *         Deferred Register Instances         *
     ***********************************************/

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SocketNukes.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SocketNukes.MODID);

    public static final DeferredRegister<TileEntityType<?>> TETYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SocketNukes.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERTYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SocketNukes.MODID);

    public static final DeferredRegister<ExtendedExplosionType> EXPLOSIONS = DeferredRegister.create(ExtendedExplosionType.class, SocketNukes.MODID);

    /***********************************************
     *            SocketNukes Registries           *
     **********************************************/

    public static Supplier<IForgeRegistry<ExtendedExplosionType>> EXPLOSION_TYPE_REGISTRY = EXPLOSIONS.makeRegistry("explosion_types", () ->
            new RegistryBuilder<ExtendedExplosionType>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, old) -> SocketNukes.LOGGER.info("ExplosionType Added: " + id + " "))
    );

    /***********************************************
     *          Registry Object Instances          *
     ***********************************************/

    public static final RegistryObject<Item> EXPLODER_ITEM = ITEMS.register("exploder_item", () -> new ExploderItem(SocketItems.EXPLODER_PROPERTIES));

    public static final RegistryObject<VanillaExplosionType> VANILLA_EXPLOSION = EXPLOSIONS.register("vanilla_explosion", () ->
        new VanillaExplosionType(new ExplosionProperties(true, false, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE))
    );

    public static void initialize() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        TETYPES.register(modBus);
        CONTAINERTYPES.register(modBus);
        EXPLOSIONS.register(modBus);
    }
}
