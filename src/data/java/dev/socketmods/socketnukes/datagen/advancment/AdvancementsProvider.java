package dev.socketmods.socketnukes.datagen.advancment;

import dev.socketmods.socketnukes.datagen.utils.advancment.AdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.Collections;

public class AdvancementsProvider extends AdvancementProvider {

    public AdvancementsProvider(GatherDataEvent event) {
        super(event, Collections.emptyList());
    }
}
