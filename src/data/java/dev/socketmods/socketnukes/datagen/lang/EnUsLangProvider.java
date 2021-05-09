package dev.socketmods.socketnukes.datagen.lang;

import java.util.Objects;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.data.LanguageProvider;

public class EnUsLangProvider extends LanguageProvider {

    public EnUsLangProvider(DataGenerator generatorIn) {
        super(generatorIn, SocketNukes.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(SNRegistry.EXPLODER_ITEM.get(), "Exploderiser 9000");
        add(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get(), "Pop Filter");

        add(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), "Pop Filter");
        add(SNRegistry.BOLB_ENTITY_TYPE.get(), "Bolb");

        add(SNRegistry.VANILLA_EXPLOSION.get(), "Vanilla");
        add(SNRegistry.CUBIC_EXPLOSION.get(), "Cubic");
        add(SNRegistry.NULL_EXPLOSION.get(), "Null");
        add(SNRegistry.BOLB_EXPLOSION.get(), "Bolb");

        add(SocketItems.SOCKETNUKES_GROUP, "SocketNukes - Explosives");

        add("chat.socketmods.not_enough_energy", "Not enough energy");
        add("chat.socketmods.found.miss", "Nothing in range");
        add("chat.socketmods.found.block", "Detected block at %s");

        add("socketnukes.title.exploderconfig", "Exploderiser Configuration");
    }

    private void add(ItemGroup group, String name) {
        add("itemGroup." + Objects.requireNonNull(group.getPath()), name);
    }

    private void add(ExtendedExplosionType type, String name) {
        add(SNRegistry.getName(type).getNamespace() + ".explosions." + SNRegistry.getName(type).getPath(), name);
    }
}
