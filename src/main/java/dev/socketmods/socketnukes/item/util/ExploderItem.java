package dev.socketmods.socketnukes.item.util;

import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.client.screen.ExploderConfigScreen;
import dev.socketmods.socketnukes.explosion.DummyExplosion;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ExploderItem extends Item {

    public ExploderItem(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {

        if(context.getWorld().isRemote && context.getPlayer().isCrouching())
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().displayGuiScreen(new ExploderConfigScreen()));

        if(context.getWorld().getBlockState(context.getPos()).getBlock() == SNRegistry.VANILLA_EXPLOSIVE.get())
            return super.onItemUse(context);

        context.getItem().getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap -> {
            ExtendedExplosionType explosion = SNRegistry.parseExplosion(cap.getConfig());

            DummyExplosion dummy = new DummyExplosion(context.getWorld(), context.getPlayer(),
                    context.getPos().getX(), context.getPos().getY(), context.getPos().getZ(),
                    explosion);

            dummy.runExplosion();
        });

        return super.onItemUse(context);
    }

}
