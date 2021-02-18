package dev.socketmods.socketnukes.item.util;

import dev.socketmods.socketnukes.explosion.types.VanillaExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

public class ExploderItem extends Item {

    public ExploderItem(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        VanillaExplosionType explosion = SNRegistry.VANILLA_EXPLOSION.get();

        explosion.prepareExplosion(context.getWorld(), context.getPos(), context.getPlayer());

        for (int stage = 0; stage < explosion.getExplosionStages(); stage++) {
            explosion.explode(context.getWorld(), context.getPos(), stage + 1, context.getPlayer());

        }
        return super.onItemUse(context);
    }
}
