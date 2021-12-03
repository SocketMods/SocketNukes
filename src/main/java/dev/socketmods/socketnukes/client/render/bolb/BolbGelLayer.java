package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;

public class BolbGelLayer extends RenderLayer<BolbEntity, BolbModel> {
    private final BolbModel bolbModel = new BolbModel(0);

    public BolbGelLayer(RenderLayerParent<BolbEntity, BolbModel> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffers, int packedLight, BolbEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) return;

        this.getParentModel().copyPropertiesTo(this.bolbModel);
        this.bolbModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.bolbModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer builder = buffers.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
        this.bolbModel.renderToBuffer(stack, builder, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }
}