package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.utils.Bolbs;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

/**
 * Render the bolbmas hat on Bolbs called Curle.
 *
 * @author Citrine
 */
public class BolbHatLayer extends RenderLayer<BolbEntity, BolbModel> {

    public BolbHatLayer(RenderLayerParent<BolbEntity, BolbModel> renderManager) {
        super(renderManager);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffers, int packedLight, BolbEntity bolb, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (bolb.isInvisible()) return;
        if (!Bolbs.isCurle(bolb)) return;

        VertexConsumer builder = buffers.getBuffer(RenderType.entitySolid(bolb.getEntityTexture()));

        int packedOverlay = LivingEntityRenderer.getOverlayCoords(bolb, 0.0F);

        stack.pushPose();
        stack.translate(-0.1, 1D, -0.1D);
        stack.scale(0.7F, 0.7F, 0.7F);

        this.getParentModel().renderHat(stack, builder, packedLight, packedOverlay);
        stack.popPose();
    }

}