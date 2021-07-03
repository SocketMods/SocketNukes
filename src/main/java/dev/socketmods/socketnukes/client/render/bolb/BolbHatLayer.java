package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.utils.Bolbs;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

/**
 * Render the bolbmas hat on Bolbs called Curle.
 *
 * @author Citrine
 */
public class BolbHatLayer extends LayerRenderer<BolbEntity, BolbModel> {

    public BolbHatLayer(IEntityRenderer<BolbEntity, BolbModel> renderManager) {
        super(renderManager);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffers, int packedLight, BolbEntity bolb, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (bolb.isInvisible()) return;
        if (!Bolbs.isCurle(bolb)) return;

        IVertexBuilder builder = buffers.getBuffer(RenderType.entitySolid(bolb.getEntityTexture()));

        int packedOverlay = LivingRenderer.getOverlayCoords(bolb, 0.0F);

        stack.pushPose();
        stack.translate(-0.1, 1D, -0.1D);
        stack.scale(0.7F, 0.7F, 0.7F);

        this.getParentModel().renderHat(stack, builder, packedLight, packedOverlay);
        stack.popPose();
    }

}