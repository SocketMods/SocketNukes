package dev.socketmods.socketnukes.item;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * The name here is a little bit of a misnomer.
 * It contains Item.Properties instances for reuse, plus the tabs.
 * Item instances are in SNRegistry.
 */
public class SocketItems {

    public static CreativeModeTab SOCKETNUKES_GROUP = CreativeModeTab.builder()
            .title(Component.translatable(SocketNukes.MODID + ".main"))
            .icon(() -> new ItemStack(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get()))
            .displayItems((p_270258_, p_259752_) -> {
                p_259752_.accept(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get());
                p_259752_.accept(SNRegistry.EXPLODER_ITEM.get());
            }).build();


    /***********************************************
     *           Item Properties Instances         *
     ***********************************************/

    public static final Item.Properties EXPLODER_PROPERTIES = new Item.Properties().stacksTo(1);
    public static final Item.Properties EXPLOSIVE_PROPERTIES = new Item.Properties();

}
