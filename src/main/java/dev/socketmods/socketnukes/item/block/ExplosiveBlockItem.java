package dev.socketmods.socketnukes.item.block;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

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




    // TODO: Move to other place SNItems or get new name
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if(group == SocketItems.SOCKETNUKES_GROUP)
            for(RegistryObject<ExtendedExplosionType> explosionType : SNRegistry.EXPLOSIONS.getEntries()) {
                ItemStack newItem = new ItemStack(this);
                CompoundTag tag = newItem.getOrCreateTagElement(SocketNukes.MODID);
                tag.putString("explosion", SNRegistry.EXPLOSION_TYPE_REGISTRY.get().getKey(explosionType.get()).toString());
                items.add(newItem);
            }
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
