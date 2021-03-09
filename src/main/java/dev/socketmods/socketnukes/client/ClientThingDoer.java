package dev.socketmods.socketnukes.client;

import dev.socketmods.socketnukes.client.screen.ExploderConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

public class ClientThingDoer {

    public static void openConfigScreen() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().displayGuiScreen(new ExploderConfigScreen()));
    }
}
