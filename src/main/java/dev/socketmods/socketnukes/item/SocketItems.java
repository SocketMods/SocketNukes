package dev.socketmods.socketnukes.item;

import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

/**
 * The name here is a little bit of a misnomer.
 * It contains Item.Properties instances for reuse, plus the tabs.
 *
 * Item instances are in SNRegistry.
 */

public class SocketItems {

    public static ItemGroup SOCKET_GROUP = new ItemGroup("socketmods.main") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(SNRegistry.GENERIC_EXPLOSIVE_ITEM.get());
        }
    };

    /***********************************************
     *           Item Properties Instances         *
     ***********************************************/

    public static final Item.Properties EXPLODER_PROPERTIES = new Item.Properties().maxStackSize(1).group(SOCKET_GROUP);
    public static final Item.Properties EXPLOSIVE_PROPERTIES = new Item.Properties().group(SOCKET_GROUP);


}
