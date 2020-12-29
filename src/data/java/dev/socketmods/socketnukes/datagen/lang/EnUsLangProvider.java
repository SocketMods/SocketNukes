package dev.socketmods.socketnukes.datagen.lang;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnUsLangProvider extends LanguageProvider {

    public EnUsLangProvider(DataGenerator generatorIn) {
        super(generatorIn, SocketNukes.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(SNRegistry.EXPLODER_ITEM.get(), "Exploderiser 9000");

    }
}
