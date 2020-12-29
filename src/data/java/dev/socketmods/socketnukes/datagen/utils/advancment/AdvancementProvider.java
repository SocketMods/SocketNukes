package dev.socketmods.socketnukes.datagen.utils.advancment;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AdvancementProvider implements IDataProvider {
  private static final Logger LOGGER = LogManager.getLogger();
  private final DataGenerator generator;
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

  public AdvancementProvider(DataGenerator generatorIn) {
    this.generator = generatorIn;
  }

  @Override
  public void act(DirectoryCache cache) {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    registerAdvancement((advancement) -> {
      if (!set.add(advancement.getId())) {
        throw new IllegalStateException("Duplicate advancement " + advancement.getId());
      } else {
        Path path1 = getPath(path, advancement);

        try {
          IDataProvider.save(GSON, cache, advancement.copy().serialize(), path1);
        } catch (IOException ioexception) {
          LOGGER.error("Couldn't save advancement {}", path1, ioexception);
        }

      }
    });
  }

  protected abstract void registerAdvancement(Consumer<Advancement> consumer);

  private static Path getPath(Path pathIn, Advancement advancementIn) {
    return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
  }

}
