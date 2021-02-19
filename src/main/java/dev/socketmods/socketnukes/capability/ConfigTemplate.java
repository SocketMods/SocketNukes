package dev.socketmods.socketnukes.capability;

/**
 * A template for the getter/setter for the Exploder Configuration.
 *
 * Exploder Configuration is a string that represents the currently selected explosion in the Exploder Item.
 *
 * @author Citrine
 */
public class ConfigTemplate implements IConfiguration {

    // For safety, the configuration defaults to Null, which does nothing but resolves properly.
    private String config = "null";

    /**
     * @return the current config value.
     */
    @Override
    public String getConfig() {
        return config;
    }

    /**
     * Set a new configuration value.
     * @param config the new config value
     */
    @Override
    public void setConfig(String config) {
        this.config = config;
    }
}
