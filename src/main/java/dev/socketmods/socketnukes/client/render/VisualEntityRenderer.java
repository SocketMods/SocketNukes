package dev.socketmods.socketnukes.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.socketmods.socketnukes.entity.VisualActorEntity;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TNTMinecartRenderer;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

/**
 * Renderer class for Explosive Entities.
 * They are much like TNT, flashing white a few times before swelling and exploding.
 * The only difference is the texture.
 * Thus, this class is mostly identical to TNTRenderer.
 * @author Citrine
 */
public class VisualEntityRenderer extends EntityRenderer<VisualActorEntity> {
    public VisualEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    @Override
    public void render(VisualActorEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.translate(0.0D, 0.5D, 0.0D);

        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-90.0F));
        matrixStackIn.translate(-0.5D, -0.5D, 0.5D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F));
        TNTMinecartRenderer.renderTntFlash(SNRegistry.GENERIC_EXPLOSIVE.get().getDefaultState(), matrixStackIn, bufferIn, packedLightIn, false);
        matrixStackIn.pop();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    /**
     * TODO: Fix this to take on the form of the block that created it?
     */
    @Override
    public ResourceLocation getEntityTexture(VisualActorEntity entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }

}
