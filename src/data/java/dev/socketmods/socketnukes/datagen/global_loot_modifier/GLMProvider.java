package dev.socketmods.socketnukes.datagen.global_loot_modifier;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class GLMProvider extends GlobalLootModifierProvider {

    public GLMProvider(PackOutput output) {
        super(output, SocketNukes.MODID);
    }

    @Override
    protected void start() {

    }
}
