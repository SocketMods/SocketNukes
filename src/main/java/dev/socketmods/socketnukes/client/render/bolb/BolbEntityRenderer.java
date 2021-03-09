package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class BolbEntityRenderer extends MobRenderer<BolbEntity, BolbModel<BolbEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SocketNukes.MODID, "textures/entity/bolb.png");

    public BolbEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BolbModel<>(16), 0.25F);
        this.addLayer(new BolbGelLayer<>(this));
        this.addLayer(new BolbHatLayer<>(this));
    }

    public void render(BolbEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.shadowSize = 0.25F * (float)entityIn.getSlimeSize();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void preRenderCallback(BolbEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.999F, 0.999F, 0.999F);
        matrixStackIn.translate(0.0D, 0.001F, 0.0D);
        float f1 = (float)entitylivingbaseIn.getSlimeSize();
        float f2 = MathHelper.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(BolbEntity entity) {
        return TEXTURE;
    }

    public static ResourceLocation getEntityTextureLocation(BolbEntity entity) {
        return TEXTURE;
    }
}

