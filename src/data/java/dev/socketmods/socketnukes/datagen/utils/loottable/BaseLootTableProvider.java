package dev.socketmods.socketnukes.datagen.utils.loottable;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public abstract class BaseLootTableProvider extends LootTableProvider {

  private static final Logger LOGGER = LogManager.getLogger();

  protected final Set<Map<Block, LootTable.Builder>> lootTables = new HashSet<>();
  public static Map<ResourceLocation, LootTable> tables = new HashMap<>();

  public BaseLootTableProvider(PackOutput output, Set<ResourceLocation> requiredTableNames, List<LootTableProvider.SubProviderEntry> providers) {
    super(
            output,
            // specify registry names of the tables that are required to generate, or can leave empty
            requiredTableNames,
            // Sub providers which generate the loot
            providers
    );
  }

  public static LootTable.Builder createStandardBlockTable(String name, Block block, BlockEntityType<?> blockEntityType) {
    LootPool.Builder builder = LootPool.lootPool()
        .name(name)
        .setRolls(ConstantValue.exactly(1))
        .add(LootItem.lootTableItem(block)
            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
            .apply(SetContainerContents.setContents(blockEntityType)
                .withEntry(DynamicLoot.dynamicEntry(new ResourceLocation("minecraft", "contents"))))
        );
    return LootTable.lootTable().withPool(builder).setParamSet(LootContextParamSets.BLOCK);
  }
}
