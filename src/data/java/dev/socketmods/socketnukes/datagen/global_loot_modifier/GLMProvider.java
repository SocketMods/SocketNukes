package dev.socketmods.socketnukes.datagen.global_loot_modifier;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class GLMProvider extends GlobalLootModifierProvider {


    public GLMProvider(DataGenerator generatorIn) {
        super(generatorIn, SocketNukes.MODID);
    }

    @Override
    protected void start() {

    }
}
