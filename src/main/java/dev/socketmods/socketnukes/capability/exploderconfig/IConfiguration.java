package dev.socketmods.socketnukes.capability.exploderconfig;

import net.minecraft.resources.ResourceLocation;

public interface IConfiguration {
    void setConfig(ResourceLocation resLoc);

    ResourceLocation getConfig();
}
