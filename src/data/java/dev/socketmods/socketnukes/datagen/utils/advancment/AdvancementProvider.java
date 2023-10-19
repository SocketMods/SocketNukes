package dev.socketmods.socketnukes.datagen.utils.advancment;

import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class AdvancementProvider extends ForgeAdvancementProvider {
  private static final Logger LOGGER = LogManager.getLogger();

  public AdvancementProvider(GatherDataEvent event, List<ForgeAdvancementProvider.AdvancementGenerator> provider) {
    super(
            event.getGenerator().getPackOutput(),
            event.getLookupProvider(),
            event.getExistingFileHelper(),
            provider
    );
  }

}
