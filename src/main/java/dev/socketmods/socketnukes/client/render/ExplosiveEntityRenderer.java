package dev.socketmods.socketnukes.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.socketmods.socketnukes.entity.ExplosiveEntity;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Renderer class for Explosive Entities.
 * They are much like TNT, flashing white a few times before swelling and exploding.
 * The only difference is the texture.
 * Thus, this class is mostly identical to TNTRenderer.
 * @author Citrine
 */
public class ExplosiveEntityRenderer extends EntityRenderer<ExplosiveEntity> {
    public ExplosiveEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowRadius = 0.5F;
    }

    @Override
    public void render(ExplosiveEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);
        if ((float)entityIn.getFuse() - partialTicks + 1.0F < 10.0F) {
            float f = 1.0F - ((float)entityIn.getFuse() - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            matrixStackIn.scale(f1, f1, f1);
        }

        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderWhiteSolidBlock(SNRegistry.GENERIC_EXPLOSIVE.get().defaultBlockState(), matrixStackIn, bufferIn, packedLightIn, entityIn.getFuse() / 5 % 2 == 0);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * TODO: Fix this to take on the form of the block that created it?
     */
    @Override
    public ResourceLocation getTextureLocation(ExplosiveEntity entity) {
        return PlayerContainer.BLOCK_ATLAS;
    }

}
