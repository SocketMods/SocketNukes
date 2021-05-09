package dev.socketmods.socketnukes.capability.exploderconfig;

import java.util.Objects;
import javax.annotation.Nullable;

import dev.socketmods.socketnukes.capability.Capabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
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
public class ConfigProvider implements ICapabilitySerializable<CompoundNBT> {

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
    public CompoundNBT serializeNBT() {
        if(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY == null) {
            return new CompoundNBT();
        } else {
            return (CompoundNBT) Objects.requireNonNull(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY.writeNBT(config, null));
        }
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY != null)
            Capabilities.EXPLODER_CONFIGURATION_CAPABILITY.readNBT(config, null, nbt);
    }
}
