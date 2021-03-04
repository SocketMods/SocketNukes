package dev.socketmods.socketnukes.capability;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.util.ResourceLocation;

/**
 * A template for the getter/setter for the Exploder Configuration.
 *
 * Exploder Configuration is a string that represents the currently selected explosion in the Exploder Item.
 *
 * @author Citrine
 */
public class ConfigTemplate implements IConfiguration {

    // For safety, the configuration defaults to Null, which does nothing but resolves properly.
    private ResourceLocation config = new ResourceLocation(SocketNukes.MODID, "null");

    /**
     * @return the current config value.
     */
    @Override
    public ResourceLocation getConfig() {
        return config;
    }

    /**
     * Set a new configuration value.
     * @param config the new config value
     */
    @Override
    public void setConfig(ResourceLocation config) {
        this.config = config;
    }
}
