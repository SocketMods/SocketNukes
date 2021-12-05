package dev.socketmods.socketnukes.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.socketmods.socketnukes.utils.Bolbs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import com.mojang.math.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

/**
 * Render the bolbmas hat on players deemed worthy.
 *
 * @author Citrine
 */
public class PlayerHatLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public static BakedModel HAT_MODEL = null;

    public PlayerHatLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> playerRenderer) {
        super(playerRenderer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (HAT_MODEL == null) return;
        if (player.isInvisible()) return;
        if (!Bolbs.hasHat(player)) return;

        stack.pushPose();

        if (player.hasItemInSlot(EquipmentSlot.HEAD)) stack.translate(0, -0.02f, 0);
        if (player.isCrouching()) stack.translate(0, 0.27f, 0);

        stack.mulPose(Vector3f.YP.rotationDegrees(90));
        stack.mulPose(Vector3f.XP.rotationDegrees(180));
        stack.mulPose(Vector3f.YN.rotationDegrees(netHeadYaw));
        stack.mulPose(Vector3f.ZN.rotationDegrees(headPitch));
        stack.translate(-0.3, 0.5, -0.7);

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getBlockRenderer().getModelRenderer().renderModel(stack.last(), buffer.getBuffer(RenderType.cutout()), null, HAT_MODEL, 1f, 1f, 1f, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        stack.popPose();
    }
}