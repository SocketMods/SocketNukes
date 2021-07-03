package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class BolbGelLayer extends LayerRenderer<BolbEntity, BolbModel> {
    private final BolbModel bolbModel = new BolbModel(0);

    public BolbGelLayer(IEntityRenderer<BolbEntity, BolbModel> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffers, int packedLight, BolbEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) return;

        this.getParentModel().copyPropertiesTo(this.bolbModel);
        this.bolbModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.bolbModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        IVertexBuilder builder = buffers.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
        this.bolbModel.renderToBuffer(stack, builder, packedLight, LivingRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }
}