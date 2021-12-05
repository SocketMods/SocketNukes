package dev.socketmods.socketnukes.client.render.bolb;

import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class BolbModel extends HierarchicalModel<BolbEntity> {
    protected final ModelPart root;

    public BolbModel(ModelPart root) {
        this.root = root;
    }

    public static LayerDefinition createOuterBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createInnerBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(32, 0).addBox(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(32, 4).addBox(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static LayerDefinition createHatLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        var hat1 = partdefinition.addOrReplaceChild("hat_1", CubeListBuilder.create(), PartPose.offsetAndRotation(7.7859F, -7.3333F, 8.6854F, 0.0F, 2.2253F, 0.0F));
        hat1.addOrReplaceChild("hat_1_1", CubeListBuilder.create().texOffs(18, 42).addBox(-3.8333F, 0.3333F, -1.25F, 3.0F, 2.0F, 3.0F), PartPose.ZERO);
        hat1.addOrReplaceChild("hat_1_2", CubeListBuilder.create().texOffs(0, 55).addBox(0.1667F, -2.6667F, -1.25F, 2.0F, 2.0F, 3.0F), PartPose.ZERO);
        hat1.addOrReplaceChild("hat_1_3", CubeListBuilder.create().texOffs(0, 50).addBox(-1.8333F, -0.6667F, -1.25F, 6.0F, 2.0F, 3.0F), PartPose.ZERO);

        var hat2 = partdefinition.addOrReplaceChild("hat_2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.2942F, -5.1667F, 6.4122F, 0.0F, 1.5708F, 0.0F));
        hat2.addOrReplaceChild("hat_2_1", CubeListBuilder.create().texOffs(18, 50).addBox(-1.2942F, -0.8333F, 2.5878F, 4.0F, 2.0F, 4.0F), PartPose.ZERO);
        hat2.addOrReplaceChild("hat_2_2", CubeListBuilder.create().texOffs(0, 42).addBox(-1.2942F, 1.1667F, 0.5878F, 6.0F, 2.0F, 6.0F), PartPose.ZERO);
        hat2.addOrReplaceChild("hat_2_3", CubeListBuilder.create().texOffs(0, 32).addBox(-1.2942F, 3.1667F, -1.4122F, 8.0F, 2.0F, 8.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 64);

    }

    @Override
    public void setupAnim(BolbEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public ModelPart root() {
        return root;
    }
}
