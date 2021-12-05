package dev.socketmods.socketnukes.capability.exploderconfig;

import net.minecraft.resources.ResourceLocation;

public interface IConfiguration {
    ResourceLocation getConfig();

    void setConfig(ResourceLocation resLoc);
}
