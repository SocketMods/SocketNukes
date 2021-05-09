package dev.socketmods.socketnukes.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import dev.socketmods.socketnukes.utils.ListUtils;
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

//TODO: Convert to a World Capability?
@Mod.EventBusSubscriber(modid = SocketNukes.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class StageManager extends WorldSavedData {

    private static final String NAME = SocketNukes.MODID + "_stage";

    private List<Actor> actors = new ArrayList<>();
    private final ServerWorld world;

    //------------------------------------------------------------------------------------------------------------------
    // Scaffolding
    //------------------------------------------------------------------------------------------------------------------

    private StageManager(ServerWorld world) {
        this(NAME, world);
    }

    protected StageManager(String name, ServerWorld world) {
        super(name);
        this.world = world;
    }

    @SubscribeEvent
    public static void onTickEvent(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side != LogicalSide.SERVER) return;

        get(event.world).ifPresent(StageManager::tick);
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------
    // API
    //------------------------------------------------------------------------------------------------------------------

    public static Optional<StageManager> get(World world) {
        return world instanceof ServerWorld ? Optional.of(get((ServerWorld) world)) : Optional.empty();
    }

    public static StageManager get(ServerWorld world) {
        return world.getSavedData().getOrCreate(() -> new StageManager(world), NAME);
    }

    public void add(Actor actor) {
        actors.add(actor);

        actor.onCreation(world);
    }

    public void remove(Actor actor) {
        if (actors.remove(actor)) actor.onRemoval(world);
    }

    public Stream<Actor> getActors() {
        return actors.stream();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Logic
    //------------------------------------------------------------------------------------------------------------------

    private void tick() {
        ListUtils.removeIf(actors, it -> it.tick(world), it -> it.onRemoval(world));
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
            Role<?> role = SNRegistry.getRole(data.getString("id"));
            actors.add(role.deserialize(data.getCompound("data")));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT list = new ListNBT();
        compound.put("actors", list);
        compound.putInt("size", list.size());

        for (Actor actor : actors) {
            CompoundNBT nbt = new CompoundNBT();
            Role<?> role = actor.getRole();

            nbt.putString("id", SNRegistry.getName(role).toString());
            nbt.put("data", actor.serialize(new CompoundNBT()));

            list.add(nbt);
        }

        return compound;
    }
}
