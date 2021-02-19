package dev.socketmods.socketnukes.datagen.lang;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
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
        add(SNRegistry.VANILLA_EXPLOSIVE_ITEM.get(), "Pop Filter");
        add(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), "Pop Filter");

        add(SNRegistry.VANILLA_EXPLOSION.get(), "Vanilla");
        add(SNRegistry.CUBIC_EXPLOSION.get(), "Cubic");
        add(SNRegistry.NULL_EXPLOSION.get(), "Null");
    }

    private void add(ExtendedExplosionType type, String name) {
        add(SocketNukes.MODID + ".explosions." + type.getRegistryName().getPath(), name);
    }
}
