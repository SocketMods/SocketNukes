package dev.socketmods.socketnukes.registry;

import java.util.Objects;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.block.explosive.TNTExplosive;
import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.entity.ExplosiveEntity;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.types.BolbExplosionType;
import dev.socketmods.socketnukes.explosion.types.CubicExplosionType;
import dev.socketmods.socketnukes.explosion.types.NullExplosionType;
import dev.socketmods.socketnukes.explosion.types.VanillaExplosionType;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.item.block.ExplosiveBlockItem;
import dev.socketmods.socketnukes.item.util.ExploderItem;
import dev.socketmods.socketnukes.tileentity.ExplosiveTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class SNRegistry {

    /***********************************************
     *         Deferred Register Instances         *
     ***********************************************/

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SocketNukes.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SocketNukes.MODID);

    public static final DeferredRegister<TileEntityType<?>> TETYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, SocketNukes.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERTYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, SocketNukes.MODID);

    public static final DeferredRegister<EntityType<?>> ENTITYTYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, SocketNukes.MODID);

    public static final DeferredRegister<ExtendedExplosionType> EXPLOSIONS = DeferredRegister.create(ExtendedExplosionType.class, SocketNukes.MODID);

    /***********************************************
     *            SocketNukes Registries           *
     **********************************************/

    public static Supplier<IForgeRegistry<ExtendedExplosionType>> EXPLOSION_TYPE_REGISTRY = EXPLOSIONS.makeRegistry("explosion_types", () ->
            new RegistryBuilder<ExtendedExplosionType>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, old) ->
                SocketNukes.LOGGER.info("ExplosionType Added: " + Objects.requireNonNull(obj.getRegistryName()).toString() + " ")
            ).setDefaultKey(new ResourceLocation(SocketNukes.MODID, "null"))
    );

    /***********************************************
     *          Registry Object Instances          *
     ***********************************************/

    // BLOCKS
    public static final RegistryObject<Block> GENERIC_EXPLOSIVE = BLOCKS.register("explosive", TNTExplosive::new);

    // ITEMS
    public static final RegistryObject<Item> EXPLODER_ITEM = ITEMS.register("exploder_item", () -> new ExploderItem(SocketItems.EXPLODER_PROPERTIES));
    public static final RegistryObject<Item> GENERIC_EXPLOSIVE_ITEM = ITEMS.register("explosive", () -> new ExplosiveBlockItem(SocketItems.EXPLOSIVE_PROPERTIES));

    // EXPLOSIONS
    public static final RegistryObject<VanillaExplosionType> VANILLA_EXPLOSION = EXPLOSIONS.register("vanilla", () ->
            new VanillaExplosionType(new ExplosionProperties(true, false, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE))
    );

    public static final RegistryObject<ExtendedExplosionType> NULL_EXPLOSION = EXPLOSIONS.register("null", NullExplosionType::new);

    public static final RegistryObject<CubicExplosionType> CUBIC_EXPLOSION = EXPLOSIONS.register("cubic", () ->
            new CubicExplosionType(new ExplosionProperties(true, false, ParticleTypes.CLOUD, SoundEvents.BLOCK_DISPENSER_FAIL))
    );

    public static final RegistryObject<BolbExplosionType> BOLB_EXPLOSION = EXPLOSIONS.register("bolb", () ->
            new BolbExplosionType(new ExplosionProperties(true, false, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, SoundEvents.BLOCK_ANVIL_LAND))
    );

    // ENTITY TYPE
    public static final RegistryObject<EntityType<ExplosiveEntity>> EXPLOSIVE_ENTITY_TYPE = ENTITYTYPES.register("explosive", () ->
        new EntityType<>(ExplosiveEntity::create, EntityClassification.MISC, true, true, false, false, ImmutableSet.of(), EntitySize.fixed(1f, 1f), 200, 1)
    );

    public static final RegistryObject<EntityType<BolbEntity>> EXPLOSIVE_BOLB_TYPE = ENTITYTYPES.register("bolb", () ->
            new EntityType<>(BolbEntity::new, EntityClassification.MISC, true, true, false, false, ImmutableSet.of(), EntitySize.flexible(2.04F, 2.04F), 10, 1)
    );

    // TILE ENTITY TYPE

    // We can't sanely provide non null data-fixers for a TileEntityType
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<TileEntityType<ExplosiveTileEntity>> EXPLOSIVE_TE = TETYPES.register("explosive", () ->
            TileEntityType.Builder.create(ExplosiveTileEntity::new, GENERIC_EXPLOSIVE.get()).build(null)
    );

    public static void initialize() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        TETYPES.register(modBus);
        CONTAINERTYPES.register(modBus);
        EXPLOSIONS.register(modBus);
        ENTITYTYPES.register(modBus);
    }
}
