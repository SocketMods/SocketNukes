package dev.socketmods.socketnukes.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.networking.Network;
import dev.socketmods.socketnukes.networking.packet.ExploderConfigChangedPacket;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

/**
 * When the Exploder Item is used, it brings up this screen.
 *
 * This screen shows a list of buttons - filled by the currently registered ExplosiveTypes, and allows you to select between them.
 * Upon a selection being made, it updates the server to make it aware.
 *
 * This is an otherwise simple class.
 *
 * @author Citrine
 */
public class ExploderConfigScreen extends Screen {

    private static final int SCREEN_WIDTH = 160;
    private static final int SCREEN_HEIGHT = 120;

    private static final ResourceLocation bg_texture = new ResourceLocation(SocketNukes.MODID, "textures/gui/exploder_config.png");

    public ExploderConfigScreen() {
        super(new TranslationTextComponent("socketnukes.title.exploderconfig"));
    }

    @Override
    protected void init() {
        int middleX = (this.width - SCREEN_WIDTH) / 2;
        int topY = SCREEN_HEIGHT - 20;
        int rollingOffset = 0;

        for(RegistryObject<ExtendedExplosionType> explosion : SNRegistry.EXPLOSIONS.getEntries()) {
            addButton(new Button(middleX + 10, topY - rollingOffset, 160, 20,
                    new TranslationTextComponent(explosion.get().getRegistryName().getNamespace() + ".explosions." + explosion.get().getRegistryName().getPath()),
                    button -> config(explosion.get().getRegistryName())));
            rollingOffset += 30;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.minecraft.getTextureManager().bindTexture(bg_texture);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void config(ResourceLocation registryName) {
        minecraft.player.getHeldItemMainhand().getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap ->
                cap.setConfig(registryName)
        );
        Network.sendToServer(new ExploderConfigChangedPacket(registryName));
    }
}
