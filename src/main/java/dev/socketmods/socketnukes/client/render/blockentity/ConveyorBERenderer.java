package dev.socketmods.socketnukes.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.socketmods.socketnukes.blockentity.ConveyorBlockEntity;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ConveyorBERenderer implements BlockEntityRenderer<ConveyorBlockEntity> {

    public ConveyorBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(ConveyorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        long millis = System.currentTimeMillis();

        pPoseStack.pushPose();
        pPoseStack.scale(.5f, .5f, .5f);
        pPoseStack.translate(1f, 0.9f, 1f);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(90));
        float distance = ((float) (millis % 200) / 100) - 1;
        pPoseStack.translate(0, distance, 0);
        itemRenderer.renderStatic(new ItemStack(SNRegistry.IRON_PLATE_ITEM.get(), 1), ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, pPackedOverlay, pPoseStack, pBuffer, Minecraft.getInstance().level, 0);
        pPoseStack.popPose();

    }
}
