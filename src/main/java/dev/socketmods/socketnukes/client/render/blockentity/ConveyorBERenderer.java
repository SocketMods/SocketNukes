package dev.socketmods.socketnukes.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.socketmods.socketnukes.block.ConveyorBlock;
import dev.socketmods.socketnukes.blockentity.ConveyorBlockEntity;
import dev.socketmods.socketnukes.registry.SNRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public class ConveyorBERenderer implements BlockEntityRenderer<ConveyorBlockEntity> {

    public ConveyorBERenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(ConveyorBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        long millis = System.currentTimeMillis();
        Direction facing = pBlockEntity.getBlockState().getValue(HorizontalDirectionalBlock.FACING);
        ConveyorBlock.Half half = pBlockEntity.getBlockState().getValue(ConveyorBlock.HALF);

        pPoseStack.pushPose();
        if (half == ConveyorBlock.Half.LONE) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
            pPoseStack.translate(1f, 0.9f, 1f);
        } else {
            pPoseStack.scale(1f, 1f, 1f);
            pPoseStack.translate(facing == Direction.EAST ? 0.5f : facing == Direction.SOUTH ? 0f : 1f, 0.5f, facing == Direction.NORTH ? 0.5f : facing == Direction.WEST ? 0f : 1f);
        }

        pPoseStack.mulPose(Axis.XP.rotationDegrees(90));
        float distance = ((float) (millis % 200) / 100) - 1;
        pPoseStack.translate(facing == Direction.EAST ? distance : facing == Direction.WEST ? -distance : 0, facing == Direction.NORTH ? -distance : facing == Direction.SOUTH ? distance : 0, 0);
        if (half == ConveyorBlock.Half.LEFT || half == ConveyorBlock.Half.LONE)
            itemRenderer.renderStatic(new ItemStack(SNRegistry.IRON_PLATE_ITEM.get(), 1), ItemDisplayContext.FIXED, LightTexture.FULL_BRIGHT, pPackedOverlay, pPoseStack, pBuffer, Minecraft.getInstance().level, 0);

        pPoseStack.popPose();
    }
}
