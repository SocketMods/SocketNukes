package dev.socketmods.socketnukes.capability.exploderconfig;

import net.minecraft.util.ResourceLocation;

public interface IConfiguration {
    void setConfig(ResourceLocation resLoc);

    ResourceLocation getConfig();
}
