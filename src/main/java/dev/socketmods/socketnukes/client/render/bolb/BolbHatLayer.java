package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.socketmods.socketnukes.client.render.SNModelLayers;
import dev.socketmods.socketnukes.entity.BolbEntity;
import dev.socketmods.socketnukes.utils.Bolbs;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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
    private final BolbModel hatModel;

    public BolbHatLayer(RenderLayerParent<BolbEntity, BolbModel> parent, EntityModelSet modelSet) {
        super(parent);
        hatModel = new BolbModel(modelSet.bakeLayer(SNModelLayers.BOLB_HAT));
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffers, int packedLight, BolbEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible()) return;
        if (!Bolbs.isCurle(entity)) return;

        stack.pushPose();
        stack.translate(-0.1, 1D, -0.1D);
        stack.scale(0.7F, 0.7F, 0.7F);

        this.getParentModel().copyPropertiesTo(this.hatModel);
        this.hatModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
        this.hatModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer builder = buffers.getBuffer(RenderType.entitySolid(this.getTextureLocation(entity)));
        this.hatModel.renderToBuffer(stack, builder, packedLight, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);

        stack.popPose();
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition hat1 = partdefinition.addOrReplaceChild("hat_1", CubeListBuilder.create(), PartPose.offsetAndRotation(7.7859F, -7.3333F, 8.6854F, 0.0F, 2.2253F, 0.0F));
        hat1.addOrReplaceChild("hat_1_1", CubeListBuilder.create().texOffs(18, 42).addBox(-3.8333F, 0.3333F, -1.25F, 3.0F, 2.0F, 3.0F), PartPose.ZERO);
        hat1.addOrReplaceChild("hat_1_2", CubeListBuilder.create().texOffs(0, 55).addBox(0.1667F, -2.6667F, -1.25F, 2.0F, 2.0F, 3.0F), PartPose.ZERO);
        hat1.addOrReplaceChild("hat_1_3", CubeListBuilder.create().texOffs(0, 50).addBox(-1.8333F, -0.6667F, -1.25F, 6.0F, 2.0F, 3.0F), PartPose.ZERO);

        PartDefinition hat2 = partdefinition.addOrReplaceChild("hat_2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.2942F, -5.1667F, 6.4122F, 0.0F, 1.5708F, 0.0F));
        hat2.addOrReplaceChild("hat_2_1", CubeListBuilder.create().texOffs(18, 50).addBox(-1.2942F, -0.8333F, 2.5878F, 4.0F, 2.0F, 4.0F), PartPose.ZERO);
        hat2.addOrReplaceChild("hat_2_2", CubeListBuilder.create().texOffs(0, 42).addBox(-1.2942F, 1.1667F, 0.5878F, 6.0F, 2.0F, 6.0F), PartPose.ZERO);
        hat2.addOrReplaceChild("hat_2_3", CubeListBuilder.create().texOffs(0, 32).addBox(-1.2942F, 3.1667F, -1.4122F, 8.0F, 2.0F, 8.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}