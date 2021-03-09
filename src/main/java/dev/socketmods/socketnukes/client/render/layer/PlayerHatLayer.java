package dev.socketmods.socketnukes.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class PlayerHatLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public static IBakedModel HAT_MODEL = null;

    public PlayerHatLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> p_i50923_1_) {
        super(p_i50923_1_);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, AbstractClientPlayerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if ("Dev".equals(entitylivingbaseIn.getName().getString()) && !entitylivingbaseIn.isInvisible()) {
            matrixStackIn.push();
            if (!entitylivingbaseIn.inventory.armorInventory.get(3).isEmpty())
                matrixStackIn.translate(0, -0.02f, 0);
            if (entitylivingbaseIn.isCrouching()) matrixStackIn.translate(0, 0.27f, 0);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
            matrixStackIn.rotate(Vector3f.YN.rotationDegrees(netHeadYaw));
            matrixStackIn.rotate(Vector3f.ZN.rotationDegrees(headPitch));
            matrixStackIn.translate(-0.3, 0.5, -0.7);
            Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
            if (HAT_MODEL != null) {
                Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(matrixStackIn.getLast(), bufferIn.getBuffer(RenderType.getCutout()), null, HAT_MODEL, 1f, 1f, 1f, packedLightIn, OverlayTexture.NO_OVERLAY);
            }
            matrixStackIn.pop();

        }
    }
}