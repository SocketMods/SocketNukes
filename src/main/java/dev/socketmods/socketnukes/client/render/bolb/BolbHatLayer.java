package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.socketmods.socketnukes.client.render.SNModelLayers;
import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.utils.Bolbs;
import net.minecraft.client.model.geom.EntityModelSet;
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
    private final BolbModel bolbModel;

    public BolbHatLayer(RenderLayerParent<BolbEntity, BolbModel> parent, EntityModelSet modelSet) {
        super(parent);
        bolbModel = new BolbModel(modelSet.bakeLayer(SNModelLayers.BOLB_HAT));
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffers, int packedLight, BolbEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) return;
        if (!Bolbs.isCurle(entity)) return;

        stack.pushPose();
        stack.translate(-0.1, 1D, -0.1D);
        stack.scale(0.7F, 0.7F, 0.7F);

        this.getParentModel().copyPropertiesTo(this.bolbModel);
        this.bolbModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.bolbModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer builder = buffers.getBuffer(RenderType.entitySolid(this.getTextureLocation(entity)));
        this.bolbModel.renderToBuffer(stack, builder, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);

        stack.popPose();
    }
}