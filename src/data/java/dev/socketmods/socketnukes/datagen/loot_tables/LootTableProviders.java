package dev.socketmods.socketnukes.datagen.loot_tables;

import dev.socketmods.socketnukes.datagen.utils.loottable.BaseLootTableProvider;
import net.minecraft.data.PackOutput;

import java.util.Collections;
import java.util.List;

public class LootTableProviders extends BaseLootTableProvider {

    public LootTableProviders(PackOutput output) {
        super(output, Collections.emptySet(), List.of());
    }
}
