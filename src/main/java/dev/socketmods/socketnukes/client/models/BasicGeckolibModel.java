package dev.socketmods.socketnukes.client.models;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @brief A simple animated Geckolib model. Can be reused for many purposes.
 *
 * @author Curle
 */
public class BasicGeckolibModel extends AnimatedGeoModel {
    ResourceLocation model;
    ResourceLocation texture;
    ResourceLocation anim;

    public BasicGeckolibModel(String modelPath, String texturePath, String animPath) {
        model = new ResourceLocation(SocketNukes.MODID, modelPath);
        texture = new ResourceLocation(SocketNukes.MODID, texturePath);
        anim = new ResourceLocation(SocketNukes.MODID, animPath);
    }

    @Override
    public ResourceLocation getModelLocation(Object object) {
        return model;
    }

    @Override
    public ResourceLocation getTextureLocation(Object object) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Object animatable) {
        return anim;
    }
}
