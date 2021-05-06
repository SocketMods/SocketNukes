package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class BolbEntityRenderer extends MobRenderer<BolbEntity, BolbModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SocketNukes.MODID, "textures/entity/bolb.png");

    public BolbEntityRenderer(EntityRendererManager renderManager) {
        super(renderManager, new BolbModel(16), 0.25F);
        this.addLayer(new BolbGelLayer(this));
        this.addLayer(new BolbHatLayer(this));
    }

    @Override
    public void render(BolbEntity bolb, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffers, int packedLight) {
        this.shadowSize = 0.25F * (float)bolb.getSlimeSize();
        super.render(bolb, entityYaw, partialTicks, stack, buffers, packedLight);
    }

    @Override
    protected void preRenderCallback(BolbEntity bolb, MatrixStack stack, float partialTickTime) {
        // The following code is heavily based on Vanilla's SlimeRenderer
        stack.scale(0.999F, 0.999F, 0.999F);
        stack.translate(0.0D, 0.001F, 0.0D);

        float size   = bolb.getSlimeSize();
        float squish = MathHelper.lerp(partialTickTime, bolb.prevSquishFactor, bolb.squishFactor) / (size * 0.5F + 1.0F);
        float squeeze = 1.0F / (squish + 1.0F);

        stack.scale(squeeze * size, 1.0F / squeeze * size, squeeze * size);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getEntityTexture(BolbEntity bolb) {
        return getEntityTextureLocation(bolb);
    }

    public static ResourceLocation getEntityTextureLocation(BolbEntity bolb) {
        return TEXTURE;
    }
}

