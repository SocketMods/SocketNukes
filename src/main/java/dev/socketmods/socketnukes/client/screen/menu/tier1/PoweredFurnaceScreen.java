package dev.socketmods.socketnukes.client.screen.menu.tier1;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.container.tier1.PoweredFurnaceMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PoweredFurnaceScreen extends AbstractContainerScreen<PoweredFurnaceMenu> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(SocketNukes.MODID, "textures/gui/powered_furnace.png");

    public PoweredFurnaceScreen(PoweredFurnaceMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        int middleX = (width - imageWidth) / 2;
        int middleY = (height - imageHeight) / 2;

        this.blit(p_97787_, middleX, middleY, 0, 0, imageWidth, imageHeight);

        if (menu.isSmelting()) {
            this.blit(p_97787_, middleX + 86, middleY + 34, 176, 25, menu.getScaledProgress(), 36);
            this.blit(p_97787_, middleX + 66, middleY + 53, 176, 0, 14, 14);
        }
    }


    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        drawString(matrixStack, Minecraft.getInstance().font, "Energy: " + menu.getEnergyStored() + " / " + menu.getMaxEnergy(), 10, 10, 0xffffff);
    }

    @Override
    public void render(PoseStack p_97795_, int p_97796_, int p_97797_, float p_97798_) {
        renderBackground(p_97795_);
        super.render(p_97795_, p_97796_, p_97797_, p_97798_);
        renderTooltip(p_97795_, p_97796_, p_97797_);
    }
}
