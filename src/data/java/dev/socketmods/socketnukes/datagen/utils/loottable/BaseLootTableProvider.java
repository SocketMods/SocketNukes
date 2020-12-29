package dev.socketmods.socketnukes.datagen.utils.loottable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.DynamicLootEntry;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.util.ResourceLocation;
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
  private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

  protected final Set<Map<Block, LootTable.Builder>> lootTables = new HashSet<>();
  public static Map<ResourceLocation, LootTable> tables = new HashMap<>();
  protected final DataGenerator generator;

  public BaseLootTableProvider(DataGenerator generatorIn) {
    super(generatorIn);
    this.generator = generatorIn;
  }

  protected abstract void addTables();

  public static LootTable.Builder createStandardBlockTable(String name, Block block) {
    LootPool.Builder builder = LootPool.builder()
        .name(name)
        .rolls(ConstantRange.of(1))
        .addEntry(ItemLootEntry.builder(block)
            .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
            .acceptFunction(SetContents.builderIn()
                .addLootEntry(DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents"))))
        );
    return LootTable.builder().addLootPool(builder).setParameterSet(LootParameterSets.BLOCK);
  }

  @Override
  public void act(DirectoryCache cache) {
    addTables();

    lootTables.forEach(blockBuilderMap -> {
      for (Map.Entry<Block, LootTable.Builder> entry : blockBuilderMap.entrySet()) {
        tables.put(entry.getKey().getLootTable(), entry.getValue().build());
      }
    });

    writeTables(cache, tables);
  }

  private void writeTables(DirectoryCache cache, Map<ResourceLocation, LootTable> tables) {
    Path outputFolder = this.generator.getOutputFolder();
    tables.forEach((key, lootTable) -> {
      Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
      try {
        IDataProvider.save(GSON, cache, LootTableManager.toJson(lootTable), path);
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
