package vmua.skystone.entity;

import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory; // ИЗМЕНЕНО НА SIDED
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import vmua.skystone.ModBlockEntities;
import vmua.skystone.screen.SkyStoneFurnaceScreenHandler;

import java.util.Map;

public class SkyStoneFurnaceBlockEntity extends BlockEntity implements SidedInventory, NamedScreenHandlerFactory, Tickable {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected int burnTime;
    protected int fuelTime;
    protected int cookTime;
    protected int cookTimeTotal = 133;

    private float storedExperience;

    // Направления для воронки
    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2};
    private static final int[] SIDE_SLOTS = new int[]{1};

    protected final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0: return SkyStoneFurnaceBlockEntity.this.burnTime;
                case 1: return SkyStoneFurnaceBlockEntity.this.fuelTime;
                case 2: return SkyStoneFurnaceBlockEntity.this.cookTime;
                case 3: return SkyStoneFurnaceBlockEntity.this.cookTimeTotal;
                default: return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0: SkyStoneFurnaceBlockEntity.this.burnTime = value; break;
                case 1: SkyStoneFurnaceBlockEntity.this.fuelTime = value; break;
                case 2: SkyStoneFurnaceBlockEntity.this.cookTime = value; break;
                case 3: SkyStoneFurnaceBlockEntity.this.cookTimeTotal = value; break;
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    public SkyStoneFurnaceBlockEntity() {
        super(ModBlockEntities.SKY_STONE_FURNACE_ENTITY);
    }

    @Override
    public void tick() {
        boolean originallyBurning = this.burnTime > 0;
        boolean dirty = false;

        if (this.burnTime > 0) {
            this.burnTime--;
        }

        if (!this.world.isClient) {
            ItemStack input = this.inventory.get(0);
            ItemStack fuel = this.inventory.get(1);

            boolean hasInput = !input.isEmpty();
            boolean hasFuel = !fuel.isEmpty();

            if (this.burnTime > 0 || hasFuel && hasInput) {
                SmeltingRecipe recipe = this.world.getRecipeManager()
                        .getFirstMatch(RecipeType.SMELTING, this, this.world)
                        .orElse(null);

                if (this.burnTime <= 0 && this.canSmelt(recipe)) {
                    this.burnTime = this.getFuelTime(fuel);
                    this.fuelTime = this.burnTime;
                    if (this.burnTime > 0) {
                        dirty = true;
                        if (hasFuel) {
                            // ИСПРАВЛЕНО: Логика возврата ведра для лавы!
                            Item fuelItem = fuel.getItem();
                            fuel.decrement(1);
                            if (fuel.isEmpty()) {
                                Item remainderItem = fuelItem.getRecipeRemainder();
                                if (remainderItem != null) {
                                    this.inventory.set(1, new ItemStack(remainderItem));
                                }
                            }
                        }
                    }
                }

                if (this.burnTime > 0 && this.canSmelt(recipe)) {
                    this.cookTime++;

                    int requiredCookTime = recipe != null ? (int) (recipe.getCookTime() * 0.6667) : 133;
                    this.cookTimeTotal = requiredCookTime;

                    if (this.cookTime >= this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.smelt(recipe);
                        dirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if (this.burnTime <= 0 && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (originallyBurning != (this.burnTime > 0)) {
                dirty = true;
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, this.burnTime > 0), 3);
            }
        }

        if (dirty) {
            this.markDirty();
        }
    }

    private boolean canSmelt(SmeltingRecipe recipe) {
        if (!this.inventory.get(0).isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getOutput();
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack outputSlot = this.inventory.get(2);
                if (outputSlot.isEmpty()) {
                    return true;
                } else if (outputSlot.getItem() != recipeOutput.getItem()) {
                    return false;
                } else return outputSlot.getCount() < this.getMaxCountPerStack();
            }
        } else {
            return false;
        }
    }

    private void smelt(SmeltingRecipe recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack inputSlot = this.inventory.get(0);
            ItemStack recipeOutput = recipe.getOutput();
            ItemStack outputSlot = this.inventory.get(2);

            if (outputSlot.isEmpty()) {
                this.inventory.set(2, recipeOutput.copy());
            } else if (outputSlot.getItem() == recipeOutput.getItem()) {
                outputSlot.increment(1);
            }

            this.storedExperience += recipe.getExperience() * 1.2F;
            inputSlot.decrement(1);
        }
    }

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) return 0;
        Map<net.minecraft.item.Item, Integer> map = AbstractFurnaceBlockEntity.createFuelTimeMap();
        return map.getOrDefault(fuel.getItem(), 0);
    }

    public void dropExperience(PlayerEntity player) {
        if (!this.world.isClient) {
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
        if (this.world.getBlockEntity(this.pos) != this) return false;
        return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clear() { this.inventory.clear(); }

    @Override
    public Text getDisplayName() { return new TranslatableText("container.skystone.sky_stone_furnace"); }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new SkyStoneFurnaceScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    // РЕАЛИЗАЦИЯ ИНТЕРФЕЙСА SIDEDINVENTORY ДЛЯ ВОРОНОК
    @Override
    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS; // Снизу воронка забирает только из слота 2
        } else {
            return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS; // Сверху — сырье (0), сбоку — топливо (1)
        }
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction direction) {
        if (slot == 2) return false; // Запрещаем воронкам пихать вещи в слот результата!
        if (slot == 1) return AbstractFurnaceBlockEntity.canUseAsFuel(stack); // В топливо — только топливо
        return true;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction direction) {
        if (direction == Direction.DOWN && slot == 1) {
            return stack.getItem() == Items.BUCKET; // Из слота топлива воронка снизу может достать только пустое ведро
        }
        return true;
    }

    @Override
    public void fromTag(BlockState state, NbtCompound tag) {
        super.fromTag(state, tag);
        Inventories.readNbt(tag, this.inventory);
        this.burnTime = tag.getShort("BurnTime");
        this.cookTime = tag.getShort("CookTime");
        this.cookTimeTotal = tag.getShort("CookTimeTotal");
        this.fuelTime = tag.getShort("FuelTime");
        this.storedExperience = tag.getFloat("StoredXp");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putShort("BurnTime", (short)this.burnTime);
        nbt.putShort("CookTime", (short)this.cookTime);
        nbt.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        nbt.putShort("FuelTime", (short)this.fuelTime);
        nbt.putFloat("StoredXp", this.storedExperience);
        Inventories.writeNbt(nbt, this.inventory);
        return nbt;
    }
}