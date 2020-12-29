package dev.socketmods.socketnukes.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * The name here is a little bit of a misnomer.
 * It contains Item.Properties instances for reuse, plus the tabs.
 *
 * Item instances are in SNRegistry.
 */

public class SocketItems {

    /***********************************************
     *           Item Properties Instances         *
     ***********************************************/

    public static final Item.Properties EXPLODER_PROPERTIES = new Item.Properties().maxStackSize(1).group(ItemGroup.MISC);


}
