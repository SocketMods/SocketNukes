package dev.socketmods.socketnukes.item.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RangefinderItem extends Item {
    private static final int MAX_DISTANCE = 100; // TODO: configuration?
    private static final int ENERGY_PER_USE = 100;
    private static final int ENERGY_CAPACITY = 5000;
    private static final int ENERGY_MAX_RECEIVE = 100;
    private static final int ENERGY_MAX_EXTRACT = 100;

    public RangefinderItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);

        if (!world.isRemote()) return ActionResult.resultPass(heldStack);

        IEnergyStorage energy = heldStack.getCapability(CapabilityEnergy.ENERGY)
                .orElseThrow(() -> new IllegalStateException("Rangefinder item does not have energy capability"));

        if (energy.extractEnergy(ENERGY_PER_USE, true) != ENERGY_PER_USE) {
            player.sendStatusMessage(new TranslationTextComponent("chat.socketmods.not_enough_energy")
                    .mergeStyle(TextFormatting.RED), true);
            return ActionResult.resultFail(heldStack);
        }
        energy.extractEnergy(ENERGY_PER_USE, false);

        BlockRayTraceResult result = rayTrace(world, player, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.ANY, MAX_DISTANCE);

        if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = result.getPos();

            ITextComponent coordinates = TextComponentUtils
                    .wrapWithSquareBrackets(new TranslationTextComponent("chat.coordinates", pos.getX(), pos.getY(), pos.getZ()))
                    .modifyStyle((s) -> s.setFormatting(TextFormatting.GREEN)
                            .setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, pos.getX() + " " + pos.getY() + " " + pos.getZ()))
                            .setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslationTextComponent("chat.copy.click"))));

            player.sendStatusMessage(new TranslationTextComponent("chat.socketmods.found.block", coordinates), false);
            return ActionResult.func_233538_a_(heldStack, world.isRemote());
        }

        player.sendStatusMessage(new TranslationTextComponent("chat.socketmods.found.miss")
                .mergeStyle(TextFormatting.RED), true);
        return ActionResult.resultFail(heldStack);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).map(s -> (double) s.getEnergyStored() / (double) s.getMaxEnergyStored()).orElse(0.0D);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new RangefinderCapabilityProvider();
    }

    public static BlockRayTraceResult rayTrace(World worldIn, PlayerEntity player, RayTraceContext.BlockMode blockMode, RayTraceContext.FluidMode fluidMode, double distance) {
        float pitch = player.rotationPitch;
        float yaw = player.rotationYaw;
        Vector3d startVector = player.getEyePosition(1.0F);
        float f2 = MathHelper.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = MathHelper.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -MathHelper.cos(-pitch * ((float) Math.PI / 180F));
        float f5 = MathHelper.sin(-pitch * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        Vector3d endVector = startVector.add(f6 * distance, f5 * distance, f7 * distance);
        return worldIn.rayTraceBlocks(new RayTraceContext(startVector, endVector, blockMode, fluidMode, player));
    }

    static class RangefinderCapabilityProvider implements ICapabilitySerializable<IntNBT> {
        private EnergyStorage energyStorage = new EnergyStorage(ENERGY_CAPACITY, ENERGY_MAX_RECEIVE, ENERGY_MAX_EXTRACT);
        private final LazyOptional<IEnergyStorage> energyStorageCap = LazyOptional.of(() -> energyStorage);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return CapabilityEnergy.ENERGY.orEmpty(cap, energyStorageCap);
        }

        @Override
        public IntNBT serializeNBT() {
            return IntNBT.valueOf(energyStorage.getEnergyStored());
        }

        @Override
        public void deserializeNBT(IntNBT nbt) {
            energyStorage = new EnergyStorage(ENERGY_CAPACITY, ENERGY_MAX_RECEIVE, ENERGY_MAX_EXTRACT, nbt.getInt());
        }
    }
}
