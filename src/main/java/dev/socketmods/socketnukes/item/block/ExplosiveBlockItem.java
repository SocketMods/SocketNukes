package dev.socketmods.socketnukes.item.block;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

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

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == SocketItems.SOCKETNUKES_GROUP)
            for(RegistryObject<ExtendedExplosionType> explosionType : SNRegistry.EXPLOSIONS.getEntries()) {
                ItemStack newItem = new ItemStack(this);
                CompoundNBT tag = newItem.getOrCreateTagElement(SocketNukes.MODID);
                tag.putString("explosion", SNRegistry.getName(explosionType).toString());
                items.add(newItem);
            }
    }

    /**
     * Override the display name to contain the name of the chosen explosion.
     * This is finnicky, and we deal with null pointers in bootstrap, so this is a little long winded.
     */
    @Override
    public ITextComponent getName(ItemStack stack) {
        ResourceLocation stackConfigLoc = new ResourceLocation(stack.getOrCreateTagElement(SocketNukes.MODID).getString("explosion"));
        ITextComponent stackConfigComponent = SNRegistry.getExplosionType(stackConfigLoc).getTranslationText();
        return new TranslationTextComponent(this.getDescriptionId(stack)).append(new StringTextComponent(" - ")).append(stackConfigComponent);
    }
}
