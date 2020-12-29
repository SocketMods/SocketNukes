package dev.socketmods.socketnukes.item.util;

import dev.socketmods.socketnukes.SocketNukes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class ExploderItem extends Item {

    public ExploderItem(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        SocketNukes.LOGGER.info("Used"); // I have no idea if this is the right event
        return super.onItemUse(context);
    }
}
