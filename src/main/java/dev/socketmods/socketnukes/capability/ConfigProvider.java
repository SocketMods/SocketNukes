package dev.socketmods.socketnukes.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class ConfigProvider implements ICapabilitySerializable<CompoundNBT> {

    private final ConfigTemplate config = new ConfigTemplate();
    private final LazyOptional<IConfiguration> configOptional = LazyOptional.of(() -> config);

    public void invalidate() {
        configOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction dir) {
        if(cap == Capabilities.EXPLODER_CONFIGURATION_CAPABILITY) {
            return configOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        if(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) Capabilities.EXPLODER_CONFIGURATION_CAPABILITY.writeNBT(config, null);
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY != null)
            Capabilities.EXPLODER_CONFIGURATION_CAPABILITY.readNBT(config, null, nbt);
    }
}
