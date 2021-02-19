package dev.socketmods.socketnukes.capability;

public class ConfigTemplate implements IConfiguration {

    private String config = "null";

    @Override
    public String getConfig() {
        return config;
    }

    @Override
    public void setConfig(String config) {
        this.config = config;
    }
}
