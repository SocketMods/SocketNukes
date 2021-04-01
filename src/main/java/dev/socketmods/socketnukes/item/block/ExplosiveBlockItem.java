package dev.socketmods.socketnukes.item.block;

import dev.socketmods.socketnukes.SocketNukes;
import dev.socketmods.socketnukes.capability.Capabilities;
import dev.socketmods.socketnukes.capability.exploderconfig.IConfiguration;
import dev.socketmods.socketnukes.item.SocketItems;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Resource;

/**
 * Multiple use block item.
 * Stores and ignites different explosions depending on what was crafted, or chosen.
 *
 * @author Citrine
 */
public class ExplosiveBlockItem extends BlockItem {

    public ExplosiveBlockItem() {
        super(SNRegistry.GENERIC_EXPLOSIVE.get(), SocketItems.EXPLOSIVE_PROPERTIES);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == SocketItems.SOCKET_GROUP)
            for(RegistryObject<ExtendedExplosionType> explosionType : SNRegistry.EXPLOSIONS.getEntries()) {
                ItemStack newItem = new ItemStack(this);
                newItem.getOrCreateChildTag(SocketNukes.MODID).putString("explosion", explosionType.get().getRegistryName().toString());
                items.add(newItem);
            }
    }

    /**
     * Override the display name to contain the name of the chosen explosion.
     * This is finnicky, and we deal with null pointers in bootstrap, so this is a little long winded.
     */
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        ResourceLocation stackConfigLoc = new ResourceLocation(stack.getOrCreateChildTag(SocketNukes.MODID).getString("explosion"));
        ITextComponent stackConfigComponent = SNRegistry.EXPLOSION_TYPE_REGISTRY.get().getValue(stackConfigLoc).getTranslationText();
        return new TranslationTextComponent(this.getTranslationKey(stack)).append(new StringTextComponent(" - ")).append(stackConfigComponent);
    }
}
