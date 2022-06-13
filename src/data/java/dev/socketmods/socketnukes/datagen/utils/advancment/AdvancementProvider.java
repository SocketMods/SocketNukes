package dev.socketmods.socketnukes.datagen.utils.advancment;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public abstract class AdvancementProvider implements DataProvider {
  private static final Logger LOGGER = LogManager.getLogger();
  private final DataGenerator generator;

  public AdvancementProvider(DataGenerator generatorIn) {
    this.generator = generatorIn;
  }

  @Override
  public void run(CachedOutput cache) {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    registerAdvancement((advancement) -> {
      if (!set.add(advancement.getId())) {
        throw new IllegalStateException("Duplicate advancement " + advancement.getId());
      } else {
        Path path1 = getPath(path, advancement);

        try {
          DataProvider.saveStable(cache, advancement.deconstruct().serializeToJson(), path1);
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
