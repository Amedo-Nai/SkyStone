package amedonai.ss_celestial_threat.entity;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeEntry; // ИСПРАВЛЕНО: Для Yarn используем RecipeEntry вместо RecipeHolder
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.recipe.input.SingleStackRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import amedonai.ss_celestial_threat.ModBlockEntities;
import amedonai.ss_celestial_threat.screen.SkyStoneFurnaceScreenHandler;

public class SkyStoneFurnaceBlockEntity extends BlockEntity implements SidedInventory, NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected int burnTime;
    protected int fuelTime;
    protected int cookTime;
    protected int cookTimeTotal = 133;

    private float storedExperience;

    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2};
    private static final int[] SIDE_SLOTS = new int[]{1};

    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> SkyStoneFurnaceBlockEntity.this.burnTime;
                case 1 -> SkyStoneFurnaceBlockEntity.this.fuelTime;
                case 2 -> SkyStoneFurnaceBlockEntity.this.cookTime;
                case 3 -> SkyStoneFurnaceBlockEntity.this.cookTimeTotal;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> SkyStoneFurnaceBlockEntity.this.burnTime = value;
                case 1 -> SkyStoneFurnaceBlockEntity.this.fuelTime = value;
                case 2 -> SkyStoneFurnaceBlockEntity.this.cookTime = value;
                case 3 -> SkyStoneFurnaceBlockEntity.this.cookTimeTotal = value;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public SkyStoneFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SKY_STONE_FURNACE_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, SkyStoneFurnaceBlockEntity blockEntity) {
        boolean originallyBurning = blockEntity.burnTime > 0;
        boolean dirty = false;

        if (blockEntity.burnTime > 0) {
            blockEntity.burnTime--;
        }

        if (!world.isClient) {
            ItemStack input = blockEntity.inventory.get(0);
            ItemStack fuel = blockEntity.inventory.get(1);

            boolean hasInput = !input.isEmpty();
            boolean hasFuel = !fuel.isEmpty();

            if (blockEntity.burnTime > 0 || hasFuel && hasInput) {
                SingleStackRecipeInput recipeInput = new SingleStackRecipeInput(input);

                // ИСПРАВЛЕНО: здесь теперь тоже RecipeEntry
                RecipeEntry<SmeltingRecipe> recipeEntry = world.getRecipeManager()
                        .getFirstMatch(RecipeType.SMELTING, recipeInput, world)
                        .orElse(null);

                if (blockEntity.burnTime <= 0 && canSmelt(world.getRegistryManager(), recipeEntry, blockEntity.inventory, blockEntity.getMaxCountPerStack())) {
                    blockEntity.burnTime = blockEntity.getFuelTime(world, fuel);
                    blockEntity.fuelTime = blockEntity.burnTime;
                    if (blockEntity.burnTime > 0) {
                        dirty = true;
                        if (hasFuel) {
                            Item fuelItem = fuel.getItem();
                            fuel.decrement(1);
                            if (fuel.isEmpty()) {
                                Item remainderItem = fuelItem.getRecipeRemainder();
                                if (remainderItem != null) {
                                    blockEntity.inventory.set(1, new ItemStack(remainderItem));
                                }
                            }
                        }
                    }
                }

                if (blockEntity.burnTime > 0 && canSmelt(world.getRegistryManager(), recipeEntry, blockEntity.inventory, blockEntity.getMaxCountPerStack())) {
                    blockEntity.cookTime++;

                    int requiredCookTime = recipeEntry != null ? (int) (recipeEntry.value().getCookingTime() * 0.6667) : 133;                    blockEntity.cookTimeTotal = requiredCookTime;

                    if (blockEntity.cookTime >= blockEntity.cookTimeTotal) {
                        blockEntity.cookTime = 0;
                        blockEntity.smelt(world.getRegistryManager(), recipeEntry);
                        dirty = true;
                    }
                } else {
                    blockEntity.cookTime = 0;
                }
            } else if (blockEntity.burnTime <= 0 && blockEntity.cookTime > 0) {
                blockEntity.cookTime = MathHelper.clamp(blockEntity.cookTime - 2, 0, blockEntity.cookTimeTotal);
            }

            if (originallyBurning != (blockEntity.burnTime > 0)) {
                dirty = true;
                world.setBlockState(pos, state.with(AbstractFurnaceBlock.LIT, blockEntity.burnTime > 0), 3);
            }
        }

        if (dirty) {
            blockEntity.markDirty();
        }
    }

    // ИСПРАВЛЕНО: RecipeEntry в параметрах метода
    private static boolean canSmelt(RegistryWrapper.WrapperLookup registries, RecipeEntry<SmeltingRecipe> recipeEntry, DefaultedList<ItemStack> inventory, int maxCount) {
        if (!inventory.get(0).isEmpty() && recipeEntry != null) {
            ItemStack recipeOutput = recipeEntry.value().getResult(registries);
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack outputSlot = inventory.get(2);
                if (outputSlot.isEmpty()) {
                    return true;
                } else if (!ItemStack.areItemsEqual(outputSlot, recipeOutput)) {
                    return false;
                } else return outputSlot.getCount() < maxCount && outputSlot.getCount() < outputSlot.getMaxCount();
            }
        } else {
            return false;
        }
    }

    // ИСПРАВЛЕНО: RecipeEntry в параметрах метода
    private void smelt(RegistryWrapper.WrapperLookup registries, RecipeEntry<SmeltingRecipe> recipeEntry) {
        if (recipeEntry != null && canSmelt(registries, recipeEntry, this.inventory, this.getMaxCountPerStack())) {
            ItemStack inputSlot = this.inventory.get(0);
            ItemStack recipeOutput = recipeEntry.value().getResult(registries);
            ItemStack outputSlot = this.inventory.get(2);

            if (outputSlot.isEmpty()) {
                this.inventory.set(2, recipeOutput.copy());
            } else if (ItemStack.areItemsEqual(outputSlot, recipeOutput)) {
                outputSlot.increment(1);
            }

            this.storedExperience += recipeEntry.value().getExperience() * 1.2F;
            inputSlot.decrement(1);
        }
    }

    protected int getFuelTime(World world, ItemStack fuel) {
        if (fuel.isEmpty()) return 0;
        return AbstractFurnaceBlockEntity.createFuelTimeMap().getOrDefault(fuel.getItem(), 0);
    }

    public void dropExperience(PlayerEntity player) {
        if (this.world != null && !this.world.isClient) {
            int xpToDrop = (int) this.storedExperience;
            this.storedExperience -= xpToDrop;
            if (xpToDrop > 0) {
                this.world.spawnEntity(new ExperienceOrbEntity(this.world, player.getX(), player.getY() + 0.5D, player.getZ(), xpToDrop));
            }
            this.markDirty();
        }
    }

    @Override
    public int size() { return this.inventory.size(); }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (!itemStack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) { return this.inventory.get(slot); }

    @Override
    public ItemStack removeStack(int slot, int amount) { return Inventories.splitStack(this.inventory, slot, amount); }

    @Override
    public ItemStack removeStack(int slot) { return Inventories.removeStack(this.inventory, slot); }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }
        this.markDirty();
    }

    @Override
    public int getMaxCountPerStack() { return 96; }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world == null || this.world.getBlockEntity(this.pos) != this) return false;
        return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clear() { this.inventory.clear(); }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.skystone.sky_stone_furnace");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new SkyStoneFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        } else {
            return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        if (slot == 2) return false;
        if (slot == 1) return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && slot == 1) {
            return stack.getItem() == Items.BUCKET;
        }
        return true;
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        Inventories.readNbt(nbt, this.inventory, registries);
        this.burnTime = nbt.getShort("BurnTime");
        this.cookTime = nbt.getShort("CookTime");
        this.cookTimeTotal = nbt.getShort("CookTimeTotal");
        this.fuelTime = nbt.getShort("FuelTime");
        this.storedExperience = nbt.getFloat("StoredXp");
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putShort("BurnTime", (short)this.burnTime);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        nbt.putShort("FuelTime", (short)this.fuelTime);
        nbt.putFloat("StoredXp", this.storedExperience);
        Inventories.writeNbt(nbt, this.inventory, registries);
    }
}