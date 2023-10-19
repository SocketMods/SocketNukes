package dev.socketmods.socketnukes.item;

import net.minecraft.world.item.Item;

/**
 * The name here is a little bit of a misnomer.
 * It contains Item.Properties instances for reuse, plus the tabs.
 * Item instances are in SNRegistry.
 */
public class SocketItems {


    /***********************************************
     *           Item Properties Instances         *
     ***********************************************/

    public static final Item.Properties EXPLODER_PROPERTIES = new Item.Properties().stacksTo(1);
    public static final Item.Properties EXPLOSIVE_PROPERTIES = new Item.Properties();

}
