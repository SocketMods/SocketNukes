package dev.socketmods.socketnukes.item.block;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Multiple use block item.
 * Stores and ignites different explosions depending on what was crafted, or chosen.
 *
 * @author Citrine
 */
public class ExplosiveBlockItem extends BlockItem {

    public ExplosiveBlockItem(Item.Properties properties) {
        super(SNRegistry.GENERIC_EXPLOSIVE.get(), properties);
    }
    /**
     * Override the display name to contain the name of the chosen explosion.
     * This is finnicky, and we deal with null pointers in bootstrap, so this is a little long winded.
     */
    @Override
    public Component getName(ItemStack stack) {
        ResourceLocation stackConfigLoc = new ResourceLocation(stack.getOrCreateTagElement(SocketNukes.MODID).getString("explosion"));
        Component stackConfigComponent = SNRegistry.getExplosionType(stackConfigLoc).getTranslationText();
        return Component.translatable(this.getDescriptionId(stack)).append(Component.literal(" - ")).append(stackConfigComponent);
    }
}
