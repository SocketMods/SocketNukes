package dev.socketmods.socketnukes.client;

import dev.socketmods.socketnukes.client.screen.ExploderConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class ClientThingDoer {

    public static void openConfigScreen() {
        if (Screen.hasShiftDown())
            Minecraft.getInstance().setScreen(new ExploderConfigScreen());
    }

}
