package dev.socketmods.socketnukes.item;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * The name here is a little bit of a misnomer.
 * It contains Item.Properties instances for reuse, plus the tabs.
 * <p>
 * Item instances are in SNRegistry.
 */
public class SocketItems {

    public static CreativeModeTab SOCKETNUKES_GROUP = new CreativeModeTab(SocketNukes.MODID + ".main") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get());
        }
    };

    /***********************************************
     *           Item Properties Instances         *
     ***********************************************/

    public static final Item.Properties EXPLODER_PROPERTIES = new Item.Properties().stacksTo(1).tab(SOCKETNUKES_GROUP);
    public static final Item.Properties EXPLOSIVE_PROPERTIES = new Item.Properties().tab(SOCKETNUKES_GROUP);

}
