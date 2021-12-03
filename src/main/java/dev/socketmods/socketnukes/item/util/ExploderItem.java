package dev.socketmods.socketnukes.item.util;

import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.client.ClientThingDoer;
import dev.socketmods.socketnukes.entity.ExplosiveEntity;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

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
    public InteractionResult useOn(UseOnContext context) {
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
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if(worldIn.isClientSide) ClientThingDoer.openConfigScreen();

        return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
    }
}
