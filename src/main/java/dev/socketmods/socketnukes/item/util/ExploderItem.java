package dev.socketmods.socketnukes.item.util;

import dev.socketmods.socketnukes.explosion.DummyExplosion;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
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
        ExtendedExplosionType type = context.getPlayer().isCrouching() ? SNRegistry.CUBIC_EXPLOSION.get() : SNRegistry.VANILLA_EXPLOSION.get();

        DummyExplosion explosion = new DummyExplosion(context.getWorld(), context.getPlayer(),
                context.getPos().getX(), context.getPos().getY(), context.getPos().getZ(),
                type);

        explosion.runExplosion();

        return super.onItemUse(context);
    }
}
