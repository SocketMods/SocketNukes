package dev.socketmods.socketnukes.client.screen.widget;

import java.util.function.Supplier;
import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.socketmods.socketnukes.registry.ExtendedExplosionType;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.ExtendedList;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class ExplosionList extends ExtendedList<ExplosionList.ExplosionListEntry> {

    private final FontRenderer font;

    public ExplosionList(Screen parent, int width, int height, int top, int bottom, @Nullable ResourceLocation entry) {
        this(parent, parent.getMinecraft().fontRenderer, width, height, top, bottom, entry);
    }

    public ExplosionList(Screen parent, FontRenderer font, int width, int height, int top, int bottom, @Nullable ResourceLocation entry) {
        super(parent.getMinecraft(), width, height, top, bottom, font.FONT_HEIGHT + 8);
        this.font = font;

        this.refreshList(entry);

        this.func_244605_b(false);
        this.func_244606_c(false);
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

    public class ExplosionListEntry extends ExtendedList.AbstractListEntry<ExplosionListEntry> {
        private final Supplier<ExtendedExplosionType> type;
        private final ExplosionList parent;

        ExplosionListEntry(Supplier<ExtendedExplosionType> type, ExplosionList parent) {
            this.type = type;
            this.parent = parent;
        }

        @Override
        public void render(MatrixStack stack, int idx, int top, int left, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isMouseOver, float partialTicks) {
            boolean isSelected = getSelected() == this;

            if (!isSelected) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();

                // j,   k,   j2,   k1,         j1
                // idx, top, left, entryWidth, entryHeight

                int p3 = y0 + 4 - (int)getScrollAmount();
                int i1 = p3 + idx * itemHeight + headerHeight;
                int j1 = itemHeight - 4;
                int l1 = x0 + width / 2 - entryWidth / 2;
                int i2 = x0 + width / 2 + entryWidth / 2;

                RenderSystem.disableTexture();

                float f = parent.isFocused() ? 0.25F : 0.125F;
                RenderSystem.color4f(f, f, f, 1.0F);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                bufferbuilder.pos(l1, (i1 + j1 + 2), 0.0D).endVertex();
                bufferbuilder.pos(i2, (i1 + j1 + 2), 0.0D).endVertex();
                bufferbuilder.pos(i2, (i1 - 2), 0.0D).endVertex();
                bufferbuilder.pos(l1, (i1 - 2), 0.0D).endVertex();
                tessellator.draw();

                RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
                bufferbuilder.pos((l1 + 1), (i1 + j1 + 1), 0.0D).endVertex();
                bufferbuilder.pos((i2 - 1), (i1 + j1 + 1), 0.0D).endVertex();
                bufferbuilder.pos((i2 - 1), (i1 - 1), 0.0D).endVertex();
                bufferbuilder.pos((l1 + 1), (i1 - 1), 0.0D).endVertex();
                tessellator.draw();

                RenderSystem.enableTexture();
            }

            String name = SNRegistry.getName(type).toString();
            parent.font.drawStringWithShadow(stack, name, left + 3, top + 2, isSelected ? 0xFFFF55 : 0xFFFFFF);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            setSelected(this);

            return false;
        }

        public ExtendedExplosionType getType() {
            return type.get();
        }
    }

}
