package dev.socketmods.socketnukes.registry;

import java.util.Objects;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.actors.Role;
import dev.socketmods.socketnukes.actors.dummy.DummyRole;
import dev.socketmods.socketnukes.actors.example.BolbSpawnRole;
import dev.socketmods.socketnukes.actors.example.VisualRole;
import dev.socketmods.socketnukes.block.explosive.TNTExplosive;
import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.entity.ExplosiveEntity;
import dev.socketmods.socketnukes.entity.VisualActorEntity;
import dev.socketmods.socketnukes.explosion.ExplosionProperties;
import dev.socketmods.socketnukes.explosion.types.ActorSpawnerTestExplosionType;
import dev.socketmods.socketnukes.explosion.types.BolbExplosionType;
import dev.socketmods.socketnukes.explosion.types.CubicExplosionType;
import dev.socketmods.socketnukes.explosion.types.MultiStageBolbExplosionType;
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
import net.minecraftforge.registries.IForgeRegistryEntry;
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

    public static final DeferredRegister<Role<?>> ROLES = DeferredRegister.create(Role.TYPE, SocketNukes.MODID);

    /***********************************************
     *            SocketNukes Registries           *
     **********************************************/

    public static Supplier<IForgeRegistry<ExtendedExplosionType>> EXPLOSION_TYPE_REGISTRY = EXPLOSIONS.makeRegistry("explosion_types", () ->
            new RegistryBuilder<ExtendedExplosionType>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, old) ->
                SocketNukes.LOGGER.info("ExplosionType Added: " + getName(obj).toString() + " ")
            ).setDefaultKey(new ResourceLocation(SocketNukes.MODID, "null"))
    );

    public static Supplier<IForgeRegistry<Role<?>>> ROLE_REGISTRY = ROLES.makeRegistry("roles", () ->
        new RegistryBuilder<Role<?>>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, obj, old) ->
            SocketNukes.LOGGER.info("Role Added: " + getName(obj).toString() + " ")
        ).setDefaultKey(new ResourceLocation(SocketNukes.MODID, "dummy"))
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

    public static final RegistryObject<MultiStageBolbExplosionType> BOLB_EXPLOSION_DELAY = EXPLOSIONS.register("bolb_delay", () ->
        new MultiStageBolbExplosionType(new ExplosionProperties(true, false, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, SoundEvents.BLOCK_ANVIL_LAND))
    );

    public static final RegistryObject<ActorSpawnerTestExplosionType> ACTOR_SPAWNER_TEST_EXPLOSION = EXPLOSIONS.register("actor_test_spawner", () ->
        new ActorSpawnerTestExplosionType(new ExplosionProperties(true, false, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, SoundEvents.BLOCK_ANVIL_LAND))
    );

    // ROLES
    public static final RegistryObject<Role<?>> DUMMY_ROLE = ROLES.register("dummy", DummyRole::new);
    public static final RegistryObject<Role<?>> BOLB_SPAWN_ROLE = ROLES.register("bolb_spawner", BolbSpawnRole::new);
    public static final RegistryObject<Role<?>> VISUAL_ROLE = ROLES.register("visual_test", VisualRole::new);

    // ENTITY TYPE
    public static final RegistryObject<EntityType<ExplosiveEntity>> EXPLOSIVE_ENTITY_TYPE = ENTITYTYPES.register("explosive", () ->
        new EntityType<>(ExplosiveEntity::create, EntityClassification.MISC, true, true, false, false, ImmutableSet.of(), EntitySize.fixed(1f, 1f), 200, 1)
    );

    public static final RegistryObject<EntityType<BolbEntity>> BOLB_ENTITY_TYPE = ENTITYTYPES.register("bolb", () ->
            new EntityType<>(BolbEntity::new, EntityClassification.MISC, true, true, false, false, ImmutableSet.of(), EntitySize.flexible(2.04F, 2.04F), 10, 1)
    );

    public static final RegistryObject<EntityType<VisualActorEntity>> VISUAL_TYPE = ENTITYTYPES.register("visual_test", () ->
        new EntityType<>(VisualActorEntity::new, EntityClassification.MISC, true, true, false, false, ImmutableSet.of(), EntitySize.fixed(1f, 1f), 200, 1)
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
        ROLES.register(modBus);
    }

    public static Role<?> getRole(String name) {
        return Objects.requireNonNull(ROLE_REGISTRY.get().getValue(new ResourceLocation(name)));
    }

    public static Role<?> getRole(ResourceLocation name) {
        return Objects.requireNonNull(ROLE_REGISTRY.get().getValue(name));
    }

    public static ExtendedExplosionType getExplosionType(String name) {
        return Objects.requireNonNull(EXPLOSION_TYPE_REGISTRY.get().getValue(new ResourceLocation(name)));
    }

    public static ExtendedExplosionType getExplosionType(ResourceLocation name) {
        return Objects.requireNonNull(EXPLOSION_TYPE_REGISTRY.get().getValue(name));
    }

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(T type) {
        return Objects.requireNonNull(type.getRegistryName());
    }

    public static <T extends IForgeRegistryEntry<?>> ResourceLocation getName(Supplier<T> supplier) {
        return getName(supplier.get());
    }
}
