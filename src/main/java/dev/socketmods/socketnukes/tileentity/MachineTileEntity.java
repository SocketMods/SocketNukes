package dev.socketmods.socketnukes.tileentity;

import dev.socketmods.socketnukes.recipes.CommonRecipe;
import dev.socketmods.socketnukes.recipes.ICommonRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;

public abstract class MachineTileEntity<T extends Recipe<?>> extends RecipeTileEntity<T> implements MenuProvider {

    protected int burnTime;
    protected int cookingTime;
    protected int cookingTimeTotal;
    protected int recipeUsed;
    protected boolean isBurning;
    protected ICommonRecipe recipe;

    protected ContainerData teData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> MachineTileEntity.this.isBurning() ? MachineTileEntity.this.burnTime : 0;
                case 1 -> MachineTileEntity.this.cookingTime;
                case 2 -> MachineTileEntity.this.cookingTimeTotal;
                case 3 -> MachineTileEntity.this.recipeUsed;
                default -> throw new IllegalArgumentException("Invalid index: " + index);
            };
        }

        @Override
        public void set(int index, int value) {

        }

        @Override
        public int getCount() {
            return 4;
        }
    };


    public MachineTileEntity(BlockEntityType<?> tileEntityTypeIn, RecipeType<?> recipeType, BlockPos blockPos, BlockState blockState) {
        super(tileEntityTypeIn, recipeType, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        burnTime = nbt.getInt("burnTime");
        cookingTime = nbt.getInt("cookingTime");
        cookingTimeTotal = nbt.getInt("cookingTimeTotal");
        recipeUsed = nbt.getInt("recipeUsed");
        isBurning = nbt.getBoolean("isBurning");
        super.load(nbt);
    }


    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putInt("burnTime", burnTime);
        compound.putInt("cookingTime", cookingTime);
        compound.putInt("cookingTimeTotal", cookingTimeTotal);
        compound.putInt("recipeUsed", recipeUsed);
        compound.putBoolean("isBurning", isBurning);
        super.saveAdditional(compound);
    }

    public boolean isBurning() {
        return isBurning && this.burnTime > 0;
    }

    public void setCookingTimeTotal(int cookingTimeTotal) {
        this.cookingTimeTotal = cookingTimeTotal;
    }

    public boolean setIsBurning() {
        Objects.requireNonNull(this.level);

        if (!itemHandler.getStackInSlot(getFuelSlot()).isEmpty()) {
            isBurning = true;
            if (handleBurning(itemHandler.getStackInSlot(getFuelSlot()))) {
                setChanged();
            }
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
            return true;
        }
        return false;

    }

    public abstract int getFuelSlot();

    @Override
    @Nullable
    public T getRecipe(ItemStack stack) {
        if (stack == ItemStack.EMPTY) return null;
        Objects.requireNonNull(this.level);

        Set<Recipe<?>> recipes = findRecipeByType(type, this.level);
        RecipeWrapper wrapper = new RecipeWrapper(itemHandler);
        return matching(recipes, wrapper, level);
    }

    @Override
    @Nullable
    public T getRecipeFromOutput(ItemStack result) {
        if (result == ItemStack.EMPTY) return null;
        Objects.requireNonNull(this.level);

        Set<Recipe<?>> recipes = findRecipeByType(type, this.level);

        return matchingOutput(recipes, result, level);
    }

    protected abstract T matchingOutput(Set<Recipe<?>> recipes, ItemStack result, Level world);

    protected abstract T matching(Set<Recipe<?>> recipes, RecipeWrapper wrapper, Level world);

    protected boolean handleBurning(ItemStack fuel) {
        boolean isDirty = false;
        burnTime = ForgeHooks.getBurnTime(fuel, type);
        recipeUsed = burnTime;
        if (isBurning()) {
            isDirty = true;
            if (fuel.hasCraftingRemainingItem()) {
                itemHandler.setStackInSlot(getFuelSlot(), fuel.getCraftingRemainingItem());
            } else if (!fuel.isEmpty()) {
                fuel.shrink(1);
                if (fuel.isEmpty()) {
                    itemHandler.setStackInSlot(getFuelSlot(), fuel.getCraftingRemainingItem());
                }
            }
        }
        return isDirty;
    }

    protected void smelt(@Nullable Recipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack input = this.itemHandler.getStackInSlot(0);
            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (recipe instanceof CommonRecipe) {
                output = ((CommonRecipe) recipe).getOutput().get(0);
            }
            ItemStack outputSlot = this.itemHandler.getStackInSlot(2);
            if (outputSlot.isEmpty()) {
                this.itemHandler.setStackInSlot(2, output.copy());
            } else if (outputSlot.getItem() == output.getItem()) {
                outputSlot.grow(output.getCount());
            }

            if (input.getItem() == Blocks.WET_SPONGE.asItem() && !this.itemHandler.getStackInSlot(1).isEmpty() && this.itemHandler.getStackInSlot(1).getItem() == Items.BUCKET) {
                this.itemHandler.setStackInSlot(1, new ItemStack(Items.WATER_BUCKET));
            }

            input.shrink(1);
        }
    }

    protected boolean canSmelt(@Nullable Recipe<?> recipeIn) {
        if (!this.itemHandler.getStackInSlot(0).isEmpty() && recipeIn != null) {
            ItemStack output = recipeIn.getResultItem(level.registryAccess());
            if (output.isEmpty()) {
                return false;
            } else {
                ItemStack outputSlot = this.itemHandler.getStackInSlot(2);
                if (outputSlot.isEmpty()) {
                    return true;
                } else if (!outputSlot.is(output.getItem())) {
                    return false;
                } else {
                    return outputSlot.getCount() + output.getCount() <= output.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }
            }
        } else {
            return false;
        }
    }


    public void tickServer() {
        boolean localIsBurning = isBurning();
        boolean isDirty = false;

        if (this.isBurning()) {
            --this.burnTime;
        }

        Objects.requireNonNull(this.level);
        if (!level.isClientSide) {
            ItemStack fuel = itemHandler.getStackInSlot(getFuelSlot());
            if (isBurning() || !fuel.isEmpty()) {
                ICommonRecipe recipe = (ICommonRecipe) getRecipe(itemHandler.getStackInSlot(0));
                if (this.recipe != recipe) {
                    cookingTime = 0;
                    this.recipe = recipe;
                }
                if (!this.isBurning()) {
                    isDirty = handleBurning(fuel);
                }
                if (this.isBurning() && this.canSmelt(recipe) && recipe != null) {
                    ++cookingTime;
                    if (cookingTime == cookingTimeTotal) {
                        cookingTime = 0;
                        cookingTimeTotal = recipe.getTimer();
                        smelt(recipe);
                        isDirty = true;
                    }
                } else {
                    cookingTime = 0;
                }
                if (cookingTimeTotal == 0 && !itemHandler.getStackInSlot(0).isEmpty() && recipe != null) {
                    cookingTimeTotal = recipe.getTimer();
                }
            } else if (!this.isBurning() && this.cookingTime > 0) {
                this.cookingTime = Mth.clamp(this.cookingTime - 2, 0, this.cookingTimeTotal);
            }

            if (localIsBurning != this.isBurning()) {
                if (!this.isBurning()) isBurning = false;
                isDirty = true;
                this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(AbstractFurnaceBlock.LIT, this.isBurning()), 3);
            }
        }

        if (isDirty) {
            setChanged();
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getScreenName());
    }

    protected abstract String getScreenName();
}


