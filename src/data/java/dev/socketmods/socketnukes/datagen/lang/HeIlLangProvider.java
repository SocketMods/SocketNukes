package dev.socketmods.socketnukes.datagen.lang;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class HeIlLangProvider extends LanguageProvider {
    public HeIlLangProvider(DataGenerator gen) {
        super(gen, SocketNukes.MODID, "he_il");
    }

    @Override
    protected void addTranslations() {
        add(SNRegistry.EXPLODER_ITEM.get(), "מפופצניון 9000");
    }
}
