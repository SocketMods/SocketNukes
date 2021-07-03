package dev.socketmods.socketnukes.item.util;

import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.client.ClientThingDoer;
import dev.socketmods.socketnukes.entity.ExplosiveEntity;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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
    public ActionResultType useOn(ItemUseContext context) {
        // Short-circuit if we're targeting a TNTExplosive, as the logic for that is in that class.
        if (context.getLevel().getBlockState(context.getClickedPos()).getBlock() == SNRegistry.GENERIC_EXPLOSIVE.get())
            return super.useOn(context);

        // If we're on the client and we're sneaking, open the configuration menu.
        if(!(context.getPlayer() == null) && !context.getPlayer().isCrouching()) {
            // If we're just right clicking a block, trigger an immediate explosion with the configured type.
            context.getItemInHand().getCapability(Capabilities.EXPLODER_CONFIGURATION_CAPABILITY).ifPresent(cap -> {
                ExtendedExplosionType explosion = SNRegistry.getExplosionType(cap.getConfig());

                ExplosiveEntity explosiveEntity = new ExplosiveEntity(context.getLevel(), context.getClickedPos(), explosion, context.getPlayer());
                explosiveEntity.setFuse(0);
                context.getLevel().addFreshEntity(explosiveEntity);
            });
        }
        return super.useOn(context);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(worldIn.isClientSide) ClientThingDoer.openConfigScreen();

        return ActionResult.pass(playerIn.getItemInHand(handIn));
    }
}
