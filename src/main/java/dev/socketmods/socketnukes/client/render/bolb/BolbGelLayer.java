package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

public class BolbGelLayer<T extends LivingEntity> extends LayerRenderer<T, BolbModel<T>> {
    private final EntityModel<T> bolbModel = new BolbModel<>(0);

    public BolbGelLayer(IEntityRenderer<T, BolbModel<T>> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffers, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isInvisible()) {
            this.getEntityModel().copyModelAttributesTo(this.bolbModel);
            this.bolbModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            this.bolbModel.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            IVertexBuilder builder = buffers.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(entity)));
            this.bolbModel.render(stack, builder, packedLight, LivingRenderer.getPackedOverlay(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}