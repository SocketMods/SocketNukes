package dev.socketmods.socketnukes.item.util;

import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.client.ClientThingDoer;
import dev.socketmods.socketnukes.client.screen.ExploderConfigScreen;
import dev.socketmods.socketnukes.entity.ExplosiveEntity;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

/**
 * Contains a bunch of logic for testing ExtendedExplosionTypes.
 * It can trigger immediately an explosion, or ignite a TNTExplosive with the configured type.
 * To configure it, shift-right-click a block.
 *
 * @author Curle, Citrine
 */
public class ExploderItem extends Item {

    public ExploderItem(Item.Properties props) {
        super(props);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        // Short-circuit if we're targeting a TNTExplosive, as the logic for that is in that class.
        if (context.getWorld().getBlockState(context.getPos()).getBlock() == SNRegistry.GENERIC_EXPLOSIVE.get())
            return super.onItemUse(context);

        // If we're on the client and we're sneaking, open the configuration menu.
        if(!(context.getPlayer() == null) && !context.getPlayer().isCrouching()) {
            // If we're just right clicking a block, trigger an immediate explosion with the configured type.
            context.getItem().getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap -> {
                ExtendedExplosionType explosion = SNRegistry.EXPLOSION_TYPE_REGISTRY.get().getValue(cap.getConfig());

                ExplosiveEntity explosiveEntity = new ExplosiveEntity(context.getWorld(), context.getPos(), explosion, context.getPlayer());
                explosiveEntity.setFuse(0);
                context.getWorld().addEntity(explosiveEntity);
            });
        }
        return super.onItemUse(context);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(playerIn.isCrouching() && worldIn.isRemote)
            ClientThingDoer.openConfigScreen();

        return ActionResult.resultPass(playerIn.getHeldItem(handIn));
    }
}
