package dev.socketmods.socketnukes.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SocketNukes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StageManager extends WorldSavedData {

    private static final String NAME = SocketNukes.MODID + "_stage";

    private List<Actor> actors = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    // Scaffolding
    //------------------------------------------------------------------------------------------------------------------

    private StageManager() {
        super(NAME);
    }

    protected StageManager(String name) {
        super(name);
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side != LogicalSide.SERVER) return;

        if (event.world instanceof ServerWorld) {
            ServerWorld world = (ServerWorld) event.world;

            get(world).tick(world);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // API
    //------------------------------------------------------------------------------------------------------------------

    public static Optional<StageManager> get(World world) {
        return world instanceof ServerWorld ? Optional.of(get((ServerWorld) world)) : Optional.empty();
    }

    public static StageManager get(ServerWorld world) {
        return world.getSavedData().getOrCreate(StageManager::new, NAME);
    }

    public void add(Actor effect) {
        actors.add(effect);
    }

    public void remove(Actor effect) {
        actors.remove(effect);
    }

    public Stream<Actor> getActors() {
        return actors.stream();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Logic
    //------------------------------------------------------------------------------------------------------------------

    private void tick(ServerWorld world) {
        actors.removeIf(it -> it.tick(world));
    }

    //------------------------------------------------------------------------------------------------------------------
    // Serialization
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void read(CompoundNBT nbt) {
        int size = nbt.getInt("size");
        actors = new ArrayList<>(size);

        ListNBT list = nbt.getList("actors", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT data = list.getCompound(i);
            Role<?> role = SNRegistry.getRole(nbt.getString("id"));
            actors.set(data.getInt("idx"), role.deserialize(data.getCompound("data")));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        compound.put("actors", list);
        compound.putInt("size", list.size());

        for (int i = 0; i < actors.size(); i++) {
            CompoundNBT nbt = new CompoundNBT();
            Actor actor = actors.get(i);
            Role<?> role = actor.getRole();

            nbt.putInt("idx", i);
            nbt.putString("id", SNRegistry.getName(role).toString());
            nbt.put("data", actor.serialize(new CompoundNBT()));

            list.add(nbt);
        }

        return compound;
    }
}
