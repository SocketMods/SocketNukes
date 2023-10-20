package dev.socketmods.socketnukes.registry;

import com.google.common.collect.ImmutableSet;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.block.ConveyorBlock;
import dev.socketmods.socketnukes.block.explosive.TNTExplosive;
import dev.socketmods.socketnukes.block.tier1.PoweredFurnaceBlock;
import dev.socketmods.socketnukes.blockentity.ConveyorBlockEntity;
import dev.socketmods.socketnukes.blockentity.ExplosiveBlockEntity;
import dev.socketmods.socketnukes.blockentity.tier1.PoweredFurnaceBlockEntity;
import dev.socketmods.socketnukes.container.tier1.PoweredFurnaceMenu;
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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.*;

import java.util.Objects;
import java.util.function.Supplier;

public class SNRegistry {

    /***********************************************
     *               Tag Key Instances             *
     ***********************************************/
    public static final TagKey<Item> POWER_SOURCE = ItemTags.create(new ResourceLocation(SocketNukes.MODID, "power_sources"));

    /***********************************************
     *         Deferred Register Instances         *
     ***********************************************/

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SocketNukes.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SocketNukes.MODID);

    public static final DeferredRegister<BlockEntityType<?>> TETYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SocketNukes.MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINERTYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, SocketNukes.MODID);

    public static final DeferredRegister<EntityType<?>> ENTITYTYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SocketNukes.MODID);

    public static final DeferredRegister<ExtendedExplosionType> EXPLOSIONS = DeferredRegister.create(new ResourceLocation(SocketNukes.MODID, "explosion_types"), SocketNukes.MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVETABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SocketNukes.MODID);

    /***********************************************
     *            SocketNukes Registries           *
     **********************************************/

    public static Supplier<IForgeRegistry<ExtendedExplosionType>> EXPLOSION_TYPE_REGISTRY = EXPLOSIONS.makeRegistry(() ->
            new RegistryBuilder<ExtendedExplosionType>().setMaxID(Integer.MAX_VALUE - 1).onAdd((owner, stage, id, key, obj, old) ->
                SocketNukes.LOGGER.info("ExplosionType Added: " + key.location() + " ")
            ).setDefaultKey(new ResourceLocation(SocketNukes.MODID, "null"))
    );

    /***********************************************
     *          Registry Object Instances          *
     ***********************************************/

    // BLOCKS
    public static final RegistryObject<Block> GENERIC_EXPLOSIVE = BLOCKS.register("explosive", TNTExplosive::new);
    public static final RegistryObject<Block> POWERED_FURNACE_BLOCK = BLOCKS.register("powered_furnace", PoweredFurnaceBlock::new);

    public static final RegistryObject<Block> CONVEYOR_BLOCK = BLOCKS.register("conveyor", ConveyorBlock::new);

    // ITEMS
    public static final RegistryObject<Item> EXPLODER_ITEM = ITEMS.register("exploder_item", () -> new ExploderItem(SocketItems.EXPLODER_PROPERTIES));
    public static final RegistryObject<Item> GENERIC_EXPLOSIVE_ITEM = ITEMS.register("explosive", () -> new ExplosiveBlockItem(SocketItems.EXPLOSIVE_PROPERTIES));
    public static final RegistryObject<Item> POWERED_FURNACE_ITEM = ITEMS.register("powered_furnace", () -> new BlockItem(POWERED_FURNACE_BLOCK.get(), SocketItems.EXPLOSIVE_PROPERTIES));

    public static final RegistryObject<Item> CONVEYOR_ITEM = ITEMS.register("conveyor", () -> new BlockItem(CONVEYOR_BLOCK.get(), SocketItems.CRAFTING_PROPERTIES));

    // CRAFTING ITEMS

    public static final RegistryObject<Item> IRON_PLATE_ITEM = ITEMS.register("iron_plate", () -> new Item(SocketItems.CRAFTING_PROPERTIES));

    // EXPLOSIONS
    public static final RegistryObject<VanillaExplosionType> VANILLA_EXPLOSION = EXPLOSIONS.register("vanilla", () ->
            new VanillaExplosionType(new ExplosionProperties(true, false, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.GENERIC_EXPLODE))
    );

    public static final RegistryObject<ExtendedExplosionType> NULL_EXPLOSION = EXPLOSIONS.register("null", NullExplosionType::new);

    public static final RegistryObject<CubicExplosionType> CUBIC_EXPLOSION = EXPLOSIONS.register("cubic", () ->
            new CubicExplosionType(new ExplosionProperties(true, false, ParticleTypes.CLOUD, SoundEvents.DISPENSER_FAIL))
    );

    public static final RegistryObject<BolbExplosionType> BOLB_EXPLOSION = EXPLOSIONS.register("bolb", () ->
            new BolbExplosionType(new ExplosionProperties(true, false, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, SoundEvents.ANVIL_LAND))
    );

    // CREATIVE TAB
    public static final RegistryObject<CreativeModeTab> SOCKETNUKES_GROUP = CREATIVETABS.register("sn_creative_group", () -> CreativeModeTab.builder()
            .title(Component.translatable(SocketNukes.MODID + ".main"))
            .icon(() -> new ItemStack(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get()))
            .displayItems((params, tab) -> {
                tab.accept(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get());
                tab.accept(SNRegistry.EXPLODER_ITEM.get());
                for(RegistryObject<ExtendedExplosionType> explosionType : SNRegistry.EXPLOSIONS.getEntries()) {
                    ItemStack newItem = new ItemStack(GENERIC_EXPLOSIVE_ITEM.get());
                    CompoundTag tag = newItem.getOrCreateTagElement(SocketNukes.MODID);
                    tag.putString("explosion", SNRegistry.EXPLOSION_TYPE_REGISTRY.get().getKey(explosionType.get()).toString());
                    tab.accept(newItem);
                }
                tab.accept(SNRegistry.POWERED_FURNACE_ITEM.get());
                tab.accept(SNRegistry.IRON_PLATE_ITEM.get());
                tab.accept(SNRegistry.CONVEYOR_ITEM.get());

            }).build()
    );

    public static final RegistryObject<MenuType<PoweredFurnaceMenu>> POWERED_FURNACE_MENU = CONTAINERTYPES.register("powered_furnace", () ->
            new MenuType<>((IContainerFactory<PoweredFurnaceMenu>) PoweredFurnaceMenu::new, FeatureFlagSet.of())
    );

    // ENTITY TYPE
    // TODO: Look up what FeatureFlagSet does
    public static final RegistryObject<EntityType<ExplosiveEntity>> EXPLOSIVE_ENTITY_TYPE = ENTITYTYPES.register("explosive", () ->
        new EntityType<>(ExplosiveEntity::create, MobCategory.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.fixed(1f, 1f), 200, 1, FeatureFlagSet.of())
    );

    public static final RegistryObject<EntityType<BolbEntity>> BOLB_ENTITY_TYPE = ENTITYTYPES.register("bolb", () ->
            new EntityType<>(BolbEntity::new, MobCategory.MISC, true, true, false, false, ImmutableSet.of(), EntityDimensions.scalable(2.04F, 2.04F), 10, 1, FeatureFlagSet.of())
    );

    // TILE ENTITY TYPE

    // We can't sanely provide non null data-fixers for a TileEntityType
    @SuppressWarnings("ConstantConditions")
    public static final RegistryObject<BlockEntityType<ExplosiveBlockEntity>> EXPLOSIVE_TE = TETYPES.register("explosive", () ->
            BlockEntityType.Builder.of(ExplosiveBlockEntity::new, GENERIC_EXPLOSIVE.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<PoweredFurnaceBlockEntity>> POWERED_FURNACE_BE = TETYPES.register("powered_furnace", () ->
            BlockEntityType.Builder.of(PoweredFurnaceBlockEntity::new, POWERED_FURNACE_BLOCK.get()).build(null)
    );

    public static final RegistryObject<BlockEntityType<ConveyorBlockEntity>> CONVEYOR_BE = TETYPES.register("conveyor", () ->
            BlockEntityType.Builder.of(ConveyorBlockEntity::new, CONVEYOR_BLOCK.get()).build(null)
    );

    public static void initialize() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        TETYPES.register(modBus);
        CONTAINERTYPES.register(modBus);
        EXPLOSIONS.register(modBus);
        ENTITYTYPES.register(modBus);
        CREATIVETABS.register(modBus);
    }


    public static ExtendedExplosionType getExplosionType(String name) {
        return Objects.requireNonNull(EXPLOSION_TYPE_REGISTRY.get().getValue(new ResourceLocation(name)));
    }

    public static ExtendedExplosionType getExplosionType(ResourceLocation name) {
        return Objects.requireNonNull(EXPLOSION_TYPE_REGISTRY.get().getValue(name));
    }
}
