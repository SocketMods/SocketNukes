package dev.socketmods.socketnukes.datagen.advancment;

import dev.socketmods.socketnukes.datagen.utils.advancment.AdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;

import java.util.function.Consumer;

public class AdvancementsProvider extends AdvancementProvider {
    public AdvancementsProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerAdvancement(Consumer<Advancement> consumer) {

    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
