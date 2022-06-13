package dev.socketmods.socketnukes.datagen.utils.loottable;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseLootTableProvider extends LootTableProvider {

  private static final Logger LOGGER = LogManager.getLogger();

  protected final Set<Map<Block, LootTable.Builder>> lootTables = new HashSet<>();
  public static Map<ResourceLocation, LootTable> tables = new HashMap<>();
  protected final DataGenerator generator;

  public BaseLootTableProvider(DataGenerator generatorIn) {
    super(generatorIn);
    this.generator = generatorIn;
  }

  protected abstract void addTables();

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

  @Override
  public void run(CachedOutput cache) {
    addTables();

    lootTables.forEach(blockBuilderMap -> {
      for (Map.Entry<Block, LootTable.Builder> entry : blockBuilderMap.entrySet()) {
        tables.put(entry.getKey().getLootTable(), entry.getValue().build());
      }
    });

    writeTables(cache, tables);
  }

  private void writeTables(CachedOutput cache, Map<ResourceLocation, LootTable> tables) {
    Path outputFolder = this.generator.getOutputFolder();
    tables.forEach((key, lootTable) -> {
      Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
      try {
        DataProvider.saveStable(cache, LootTables.serialize(lootTable), path);
      } catch (IOException e) {
        LOGGER.error("Couldn't write loot table {}", path, e);
      }
    });
  }

  @Override
  public String getName() {
    return SocketNukes.MODID + " LootTables";
  }
}
