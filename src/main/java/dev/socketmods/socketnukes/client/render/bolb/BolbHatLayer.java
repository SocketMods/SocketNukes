package dev.socketmods.socketnukes.client.render.bolb;

import java.util.Objects;

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
public class BolbHatLayer extends LayerRenderer<BolbEntity, BolbModel> {

    private static final String CURLE = "Curle";

    public BolbHatLayer(IEntityRenderer<BolbEntity, BolbModel> renderManager) {
        super(renderManager);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffers, int packedLight, BolbEntity bolb, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (isCurle(bolb) && !bolb.isInvisible()) {
            IVertexBuilder builder = buffers.getBuffer(RenderType.getEntitySolid(bolb.getEntityTexture()));

            int packedOverlay = LivingRenderer.getPackedOverlay(bolb, 0.0F);
            float rotationYaw = MathHelper.lerp(partialTicks, bolb.prevRotationYaw, bolb.rotationYaw) -
                MathHelper.lerp(partialTicks, bolb.prevRenderYawOffset, bolb.renderYawOffset);
            float rotationPitch = MathHelper.lerp(partialTicks, bolb.prevRotationPitch, bolb.rotationPitch);

            stack.push();

            stack.rotate(Vector3f.YP.rotationDegrees(rotationYaw));
            stack.rotate(Vector3f.XP.rotationDegrees(rotationPitch));
            stack.translate(-0.1, 1D, -0.1D);
            stack.rotate(Vector3f.XP.rotationDegrees(-rotationPitch));
            stack.rotate(Vector3f.YP.rotationDegrees(-rotationYaw));
            stack.scale(0.7F, 0.7F, 0.7F);

            this.getEntityModel().renderHat(stack, builder, packedLight, packedOverlay);
            stack.pop();
        }
    }

    public static boolean isCurle(BolbEntity bolb) {
        return Objects.equals(bolb.getName().getString(), CURLE);
    }
}