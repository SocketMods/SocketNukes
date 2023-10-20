package dev.socketmods.socketnukes.client.screen.menu.tier1;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.container.tier1.PoweredFurnaceMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PoweredFurnaceScreen extends AbstractContainerScreen<PoweredFurnaceMenu> {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(SocketNukes.MODID, "textures/gui/powered_furnace.png");

    public PoweredFurnaceScreen(PoweredFurnaceMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        int middleX = (width - imageWidth) / 2;
        int middleY = (height - imageHeight) / 2;

        graphics.blit(BACKGROUND, middleX, middleY, 0, 0, imageWidth, imageHeight);

        if (menu.isSmelting()) {
            graphics.blit(BACKGROUND, middleX + 84, middleY + 34, 176, 25, menu.getScaledProgress(), 36);
            graphics.blit(BACKGROUND, middleX + 66, middleY + 53, 176, 0, 14, 14);
        }
    }


    @Override
    protected void renderLabels(GuiGraphics matrixStack, int mouseX, int mouseY) {
        matrixStack.drawString(Minecraft.getInstance().font, "Energy: " + menu.getEnergyStored() + " / " + menu.getMaxEnergy(), 10, 10, 0xffffff);
    }

    @Override
    public void render(GuiGraphics graphics, int p_97796_, int p_97797_, float p_97798_) {
        renderBackground(graphics);
        super.render(graphics, p_97796_, p_97797_, p_97798_);
        renderTooltip(graphics, p_97796_, p_97797_);
    }
}
