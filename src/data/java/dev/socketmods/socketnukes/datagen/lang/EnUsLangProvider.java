package dev.socketmods.socketnukes.datagen.lang;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Objects;

public class EnUsLangProvider extends LanguageProvider {

    public EnUsLangProvider(DataGenerator generatorIn) {
        super(generatorIn, SocketNukes.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(SNRegistry.EXPLODER_ITEM.get(), "Exploderiser 9000");
        add(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get(), "Pop Filter");

        add(SNRegistry.EXPLOSIVE_ENTITY_TYPE.get(), "Pop Filter");
        add(SNRegistry.EXPLOSIVE_BOLB_TYPE.get(), "Bolb");

        add(SNRegistry.VANILLA_EXPLOSION.get(), "Vanilla");
        add(SNRegistry.CUBIC_EXPLOSION.get(), "Cubic");
        add(SNRegistry.NULL_EXPLOSION.get(), "Null");
        add(SNRegistry.BOLB_EXPLOSION.get(), "Bolb");

        add(SocketItems.SOCKET_GROUP, "SocketNukes - Explosives");

        // hardcode the Exploder screen title - it isn't used, but it shuts up metrics
        add("socketnukes.title.exploderconfig", "Exploderiser Configuration");
    }

    private void add(ItemGroup socketGroup, String s) {
        add(Objects.requireNonNull(socketGroup.getPath()), s);
    }

    private void add(ExtendedExplosionType type, String name) {
        add(Objects.requireNonNull(type.getRegistryName()).getNamespace() + ".explosions." + type.getRegistryName().getPath(), name);
    }
}
