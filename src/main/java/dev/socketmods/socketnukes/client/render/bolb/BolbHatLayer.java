package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Render the bolbmas hat on Bolbs called Curle.
 *
 * @author Citrine
 */
public class BolbHatLayer <T extends BolbEntity> extends LayerRenderer<T, BolbModel<T>> {
    public BolbHatLayer(IEntityRenderer<T, BolbModel<T>> p_i50923_1_) {
        super(p_i50923_1_);
    }

    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if ("Curle".equals(entitylivingbaseIn.getName().getString()) && !entitylivingbaseIn.isInvisible()) {
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntitySolid(entitylivingbaseIn.getEntityTexture()));
            int i = LivingRenderer.getPackedOverlay(entitylivingbaseIn, 0.0F);

            float f = MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRotationYaw, entitylivingbaseIn.rotationYaw) - MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRenderYawOffset, entitylivingbaseIn.renderYawOffset);
            float f1 = MathHelper.lerp(partialTicks, entitylivingbaseIn.prevRotationPitch, entitylivingbaseIn.rotationPitch);
            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f1));
            matrixStackIn.translate(-0.1, 1D, -0.1D);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-f1));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-f));
            matrixStackIn.scale(0.7F, 0.7F, 0.7F);
            this.getEntityModel().renderHat(matrixStackIn, ivertexbuilder, packedLightIn, i);
            matrixStackIn.pop();

        }
    }
}