package dev.socketmods.socketnukes.client.render.bolb;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.client.render.SNModelLayers;
import dev.socketmods.socketnukes.entity.BolbEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BolbEntityRenderer extends MobRenderer<BolbEntity, BolbModel> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(SocketNukes.MODID, "textures/entity/bolb.png");

    public BolbEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BolbModel(context.bakeLayer(SNModelLayers.BOLB)), 0.25F);
        this.addLayer(new BolbGelLayer(this, context.getModelSet()));
        this.addLayer(new BolbHatLayer(this, context.getModelSet()));
    }

    @Override
    public void render(BolbEntity bolb, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffers, int packedLight) {
        this.shadowRadius = 0.25F * (float)bolb.getSize();
        super.render(bolb, entityYaw, partialTicks, stack, buffers, packedLight);
    }

    @Override
    protected void scale(BolbEntity bolb, PoseStack stack, float partialTickTime) {
        // The following code is heavily based on Vanilla's SlimeRenderer
        stack.scale(0.999F, 0.999F, 0.999F);
        stack.translate(0.0D, 0.001F, 0.0D);

        float size   = bolb.getSize();
        float squish = Mth.lerp(partialTickTime, bolb.oSquish, bolb.squish) / (size * 0.5F + 1.0F);
        float squeeze = 1.0F / (squish + 1.0F);

        stack.scale(squeeze * size, 1.0F / squeeze * size, squeeze * size);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getTextureLocation(BolbEntity bolb) {
        return getEntityTextureLocation(bolb);
    }

    public static ResourceLocation getEntityTextureLocation(BolbEntity bolb) {
        return TEXTURE;
    }
}

