package dev.socketmods.socketnukes.actors;

import net.minecraft.nbt.CompoundNBT;

public abstract class Role<T extends Actor>  extends net.minecraftforge.registries.ForgeRegistryEntry<Role<?>>{

    public abstract T deserialize(CompoundNBT nbt);

    //TODO: public abstract Codec<T> asCodec();

    /**
     * Don't you just love Generics in Java, We need a double cast here for this to work...
     */
    @SuppressWarnings("unchecked")
    public static final Class<Role<?>> TYPE = (Class<Role<?>>)((Class<?>) Role.class);

}
