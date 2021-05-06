package dev.socketmods.socketnukes.client.render.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Render the bolbmas hat on players deemed worthy.
 *
 * @author Citrine
 */
public class PlayerHatLayer extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public static IBakedModel HAT_MODEL = null;

    public PlayerHatLayer(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> playerRenderer) {
        super(playerRenderer);
    }

    @Override
    public void render(MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (HAT_MODEL == null) return;
        if (player.isInvisible()) return;
        if (!hasHat(player)) return;

        stack.push();

        if (!player.hasItemInSlot(EquipmentSlotType.HEAD)) stack.translate(0, -0.02f, 0);
        if (player.isCrouching()) stack.translate(0, 0.27f, 0);

        stack.rotate(Vector3f.YP.rotationDegrees(90));
        stack.rotate(Vector3f.XP.rotationDegrees(180));
        stack.rotate(Vector3f.YN.rotationDegrees(netHeadYaw));
        stack.rotate(Vector3f.ZN.rotationDegrees(headPitch));
        stack.translate(-0.3, 0.5, -0.7);

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        minecraft.getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(stack.getLast(), buffer.getBuffer(RenderType.getCutout()), null, HAT_MODEL, 1f, 1f, 1f, packedLight, OverlayTexture.NO_OVERLAY);
        stack.pop();
    }

    private boolean hasHat(PlayerEntity player) {
        return SocketNukes.BOLBERS.contains(PlayerEntity.getUUID(player.getGameProfile()).toString());
    }
}