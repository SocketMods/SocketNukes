package dev.socketmods.socketnukes.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.capability.exploderconfig.IConfiguration;
import dev.socketmods.socketnukes.client.screen.widget.ExplosionList;
import dev.socketmods.socketnukes.networking.Network;
import dev.socketmods.socketnukes.networking.packet.ExploderConfigChangedPacket;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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

    private static final int SCREEN_WIDTH = 176;
    private static final int SCREEN_HEIGHT = 166;

    private static final ResourceLocation BACKGROUND = new ResourceLocation(SocketNukes.MODID, "textures/gui/exploder_config.png");

    private int guiLeft;
    private int guiTop;

    private ExplosionList list;

    public ExploderConfigScreen() {
        super(Component.translatable("socketnukes.title.exploderconfig"));
    }

    @Override
    protected void init() {
        // Due to the way the Screen / Gui System is designed we can safely assume that `minecraft` is non null here
        // The player is another story, we can assume in all normal cases it would be, the only time this can possible fail is
        // if this screen gets opened without a world.
        assert minecraft != null;
        assert minecraft.player != null;

        this.guiLeft = (this.width - SCREEN_WIDTH) / 2;
        this.guiTop = (this.height - SCREEN_HEIGHT) / 2;

        ItemStack heldItem = minecraft.player.getMainHandItem();
        ResourceLocation selected = heldItem.getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).map(IConfiguration::getConfig).orElse(null);

        int width = SCREEN_WIDTH - 10 - 5;
        int height = SCREEN_HEIGHT - 10;

        list = new ExplosionList(this, width, height, guiTop + 5, guiTop + height, selected);
        list.setLeftPos(guiLeft + 5);
        addWidget(list);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        // Due to the way the Screen / Gui System is designed we can safely assume that `minecraft` is non null here
        assert minecraft != null;

        drawCenteredString(stack, this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        RenderSystem.setShaderTexture(0, BACKGROUND);
        blit(stack, guiLeft, guiTop, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);

        double scale = minecraft.getWindow().getGuiScale();
        int posY   = guiTop + 5;
        int height = SCREEN_HEIGHT - 10;
        RenderSystem.enableScissor((int) (guiLeft * scale), (int) (posY * scale), (int) (SCREEN_WIDTH * scale), (int) (height * scale));
        list.render(stack, mouseX, mouseY, partialTicks);
        RenderSystem.disableScissor();

        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        ExplosionList.ExplosionListEntry entry = list.getSelected();
        if (entry != null)
            config(SNRegistry.EXPLOSION_TYPE_REGISTRY.get().getKey(entry.getType()));

        super.onClose();
    }

    private void config(ResourceLocation registryName) {
        // Due to the way the Screen / Gui System is designed we can safely assume that `minecraft` is non null here
        // The player is another story, we can assume in all normal cases it would be, the only time this can possible fail is
        // if this screen gets opened without a world.
        assert minecraft != null;
        assert minecraft.player != null;

        minecraft.player.getMainHandItem().getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap ->
                cap.setConfig(registryName)
        );
        Network.sendToServer(new ExploderConfigChangedPacket(registryName));
    }
}
