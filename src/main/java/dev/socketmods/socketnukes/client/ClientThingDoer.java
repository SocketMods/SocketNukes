package dev.socketmods.socketnukes.client;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.client.screen.ExploderConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class ClientThingDoer {

    public static void registerSpecialModels() {
        ModelLoader.addSpecialModel(new ResourceLocation(SocketNukes.MODID, "block/hat"));
    }

    public static void openConfigScreen() {
        if (Screen.hasShiftDown())
            Minecraft.getInstance().displayGuiScreen(new ExploderConfigScreen());
    }

}
