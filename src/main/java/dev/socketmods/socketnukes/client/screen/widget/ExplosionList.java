package dev.socketmods.socketnukes.client.screen.widget;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.*;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class ExplosionList extends ObjectSelectionList<ExplosionList.ExplosionListEntry> {

    private final Font font;

    public ExplosionList(Screen parent, int width, int height, int top, int bottom, @Nullable ResourceLocation entry) {
        this(parent, parent.getMinecraft().font, width, height, top, bottom, entry);
    }

    public ExplosionList(Screen parent, Font font, int width, int height, int top, int bottom, @Nullable ResourceLocation entry) {
        super(parent.getMinecraft(), width, height, top, bottom, font.lineHeight + 8);
        this.font = font;

        this.refreshList(entry);

        this.setRenderBackground(false);
        this.setRenderTopAndBottom(false);
        this.setRenderHeader(false, 0);
    }

    public void refreshList(@Nullable ResourceLocation selected) {
        this.clearEntries();

        SNRegistry.EXPLOSIONS.getEntries().forEach(it -> {
            ExplosionListEntry entry = new ExplosionListEntry(it, this);

            this.addEntry(entry);

            if (SNRegistry.getName(entry.getType()) == selected)
                setSelected(entry);
        });
    }

    @Override
    public int getScrollbarPosition() {
        return this.x0 + this.width;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    public class ExplosionListEntry extends ObjectSelectionList.Entry<ExplosionListEntry> {
        private final Supplier<ExtendedExplosionType> type;
        private final ExplosionList parent;

        ExplosionListEntry(Supplier<ExtendedExplosionType> type, ExplosionList parent) {
            this.type = type;
            this.parent = parent;
        }

        @Override
        public void render(PoseStack stack, int idx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            boolean isSelected = getSelected() == this;

            if (!isSelected) {
                Tesselator tessellator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuilder();

                // j,   k,   j2,   k1,         j1
                // idx, top, left, entryWidth, entryHeight

                int p3 = y0 + 4 - (int)getScrollAmount();
                int i1 = p3 + idx * itemHeight + headerHeight;
                int j1 = itemHeight - 4;
                int l1 = x0 + width / 2 - entryWidth / 2;
                int i2 = x0 + width / 2 + entryWidth / 2;

                RenderSystem.disableTexture();

                float f = parent.isFocused() ? 0.25F : 0.125F;
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                bufferbuilder.vertex(l1, (i1 + j1 + 2), 0.0D).endVertex();
                bufferbuilder.vertex(i2, (i1 + j1 + 2), 0.0D).endVertex();
                bufferbuilder.vertex(i2, (i1 - 2), 0.0D).endVertex();
                bufferbuilder.vertex(l1, (i1 - 2), 0.0D).endVertex();
                tessellator.end();

                //RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
                bufferbuilder.vertex((l1 + 1), (i1 + j1 + 1), 0.0D).endVertex();
                bufferbuilder.vertex((i2 - 1), (i1 + j1 + 1), 0.0D).endVertex();
                bufferbuilder.vertex((i2 - 1), (i1 - 1), 0.0D).endVertex();
                bufferbuilder.vertex((l1 + 1), (i1 - 1), 0.0D).endVertex();
                tessellator.end();

                RenderSystem.enableTexture();
            }

            String name = SNRegistry.getName(type).toString();
            parent.font.drawShadow(stack, name, left + 3, top + 2, isSelected ? 0xFFFF55 : 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            setSelected(this);

            return false;
        }

        public ExtendedExplosionType getType() {
            return type.get();
        }

        @Override
        public Component getNarration() {
            return getSelected().getNarration();
        }
    }

}
