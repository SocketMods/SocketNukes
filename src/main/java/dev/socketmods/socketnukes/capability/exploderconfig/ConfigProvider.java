package dev.socketmods.socketnukes.capability.exploderconfig;

import javax.annotation.Nullable;

import dev.socketmods.socketnukes.capability.Capabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * The provider for the Explosion Configuration capability.
 * Handles the logic for parsing this cap out of wherever it is needed, like NBT of a stored world.
 *
 * Exploder Configuration is a ResourceLocation that represents the currently selected explosion in the Exploder Item.
 *
 * @author Citrine
 */
public class ConfigProvider implements ICapabilitySerializable<CompoundTag> {

    private final ConfigTemplate config = new ConfigTemplate();
    private final LazyOptional<IConfiguration> configOptional = LazyOptional.of(() -> config);

    public void invalidate() {
        configOptional.invalidate();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction dir) {
        if(cap == Capabilities.EXPLODER_CONFIGURATION_CAPABILITY) {
            return configOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        if(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY == null) {
            return new CompoundTag();
        } else {
            CompoundTag tag = new CompoundTag();
            tag.putString("configuration", config.getConfig().toString());
            return tag;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY != null)
            config.setConfig(new ResourceLocation(nbt.getString("configuration")));
    }
}
