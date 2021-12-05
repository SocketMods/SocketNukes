package dev.socketmods.socketnukes.client.render.bolb;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;

public class BolbModel extends ListModel<BolbEntity> {
    protected ModelPart slimeBodies;
    protected ModelPart slimeRightEye;
    protected ModelPart slimeLeftEye;
    protected ModelPart slimeMouth;
    protected ModelPart slimeHat1;
    protected ModelPart slimeHat2;
    protected ModelPart slimeHatBase;

    public BolbModel(int slimeBodyTexOffY) {
//        this.texHeight = 64;
//        this.texWidth = 64;
//
//        this.slimeBodies = new ModelPart(this, 0, slimeBodyTexOffY);
//        this.slimeRightEye = new ModelPart(this, 32, 0);
//        this.slimeLeftEye = new ModelPart(this, 32, 4);
//        this.slimeMouth = new ModelPart(this, 32, 8);
//        slimeHatBase = new ModelPart(this);
//
//        slimeHat1 = new ModelPart(this);
//        slimeHat1.setPos(7.7859F, -7.3333F, 8.6854F);
//        slimeHatBase.addChild(slimeHat1);
//        setRotationAngle(slimeHat1, 0.0F, 2.2253F, 0.0F);
//
//        slimeHat1.texOffs(18, 42).addBox(-3.8333F, 0.3333F, -1.25F, 3.0F, 2.0F, 3.0F, 0.0F, false);
//        slimeHat1.texOffs(0, 55).addBox(0.1667F, -2.6667F, -1.25F, 2.0F, 2.0F, 3.0F, 0.0F, false);
//        slimeHat1.texOffs(0, 50).addBox(-1.8333F, -0.6667F, -1.25F, 6.0F, 2.0F, 3.0F, 0.0F, false);
//
//        slimeHat2 = new ModelPart(this);
//
//        slimeHat2.setPos(1.2942F, -5.1667F, 6.4122F);
//        slimeHatBase.addChild(slimeHat2);
//        setRotationAngle(slimeHat2, 0.0F, 1.5708F, 0.0F);
//        slimeHat2.texOffs(18, 50).addBox(-1.2942F, -0.8333F, 2.5878F, 4.0F, 2.0F, 4.0F, 0.0F, false);
//        slimeHat2.texOffs(0, 42).addBox(-1.2942F, 1.1667F, 0.5878F, 6.0F, 2.0F, 6.0F, 0.0F, false);
//        slimeHat2.texOffs(0, 32).addBox(-1.2942F, 3.1667F, -1.4122F, 8.0F, 2.0F, 8.0F, 0.0F, false);
//
//        if (slimeBodyTexOffY > 0) {
//            this.slimeBodies.addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F);
//            this.slimeRightEye.addBox(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F);
//            this.slimeLeftEye.addBox(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F);
//
//            this.slimeMouth.addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F);
//        } else {
//            this.slimeBodies.addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F);
//        }

    }

    public void renderHat(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn) {
        this.slimeHatBase.copyFrom(this.slimeBodies);
        this.slimeHatBase.x = 0.0F;
        this.slimeHatBase.y = 0.0F;
        this.slimeHatBase.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
    }

    @Override
    public void setupAnim(BolbEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }

    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
    @Override
    public Iterable<ModelPart> parts() {
        return ImmutableList.of(this.slimeBodies, this.slimeRightEye, this.slimeLeftEye, this.slimeMouth);
    }
}
