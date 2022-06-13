package dev.socketmods.socketnukes.datagen.lang;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
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
        add(SNRegistry.BOLB_ENTITY_TYPE.get(), "Bolb");

        add(SNRegistry.VANILLA_EXPLOSION.get(), "Vanilla");
        add(SNRegistry.CUBIC_EXPLOSION.get(), "Cubic");
        add(SNRegistry.NULL_EXPLOSION.get(), "Null");
        add(SNRegistry.BOLB_EXPLOSION.get(), "Bolb");

        add(SNRegistry.POWERED_FURNACE_BLOCK.get(), "Powered Furnace");
        add("blockentity.socketmods.powered_furnace", "Powered Furnace");

        add(SocketItems.SOCKETNUKES_GROUP, "SocketNukes - Explosives");

        add("socketnukes.title.exploderconfig", "Exploderiser Configuration");
    }

    private void add(CreativeModeTab group, String name) {
        add("itemGroup." + Objects.requireNonNull(group.getRecipeFolderName()), name);
    }

    private void add(ExtendedExplosionType type, String name) {
        ResourceLocation key = SNRegistry.EXPLOSION_TYPE_REGISTRY.get().getKey(type);
        add(key.getNamespace() + ".explosions." + key.getPath(), name);
    }
}
