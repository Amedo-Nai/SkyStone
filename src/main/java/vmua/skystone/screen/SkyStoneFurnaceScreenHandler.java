package vmua.skystone.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import vmua.skystone.ModScreenHandlers;
import vmua.skystone.entity.SkyStoneFurnaceBlockEntity;

public class SkyStoneFurnaceScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;

    public SkyStoneFurnaceScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3), new ArrayPropertyDelegate(4));
    }

    public SkyStoneFurnaceScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ModScreenHandlers.SKY_STONE_FURNACE, syncId);
        checkSize(inventory, 3);
        checkDataCount(propertyDelegate, 4);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;

        inventory.onOpen(playerInventory.player);

        // 0: Слот для переплавки (Сырьё) — Честные 96 для 1.16.5
        this.addSlot(new Slot(inventory, 0, 56, 17) {
            @Override
            public int getMaxItemCount() {
                return 96;
            }
        });

        // 1: Слот для топлива — Лимит 96
        this.addSlot(new Slot(inventory, 1, 56, 53) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return AbstractFurnaceBlockEntity.canUseAsFuel(stack);
            }

            @Override
            public int getMaxItemCount() {
                return 96;
            }
        });

        // 2: Слот для результата — Лимит 96
        this.addSlot(new Slot(inventory, 2, 116, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public int getMaxItemCount() {
                return 96;
            }

            @Override
            public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
                if (inventory instanceof SkyStoneFurnaceBlockEntity) {
                    ((SkyStoneFurnaceBlockEntity) inventory).dropExperience(player);
                }
                return super.onTakeItem(player, stack);
            }
        });

        // Инвентарь игрока
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        // Хотбар игрока
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }

        this.addProperties(propertyDelegate);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    // Переопределяем клики по слотам печки, чтобы разрешить ручную укладку до 96 штук
    @Override
    public ItemStack onSlotClick(int slotId, int clickData, SlotActionType actionType, PlayerEntity player) {
        if (slotId >= 0 && slotId < 3 && actionType == SlotActionType.PICKUP) {
            Slot slot = this.slots.get(slotId);
            ItemStack cursorStack = player.inventory.getCursorStack();
            ItemStack slotStack = slot.getStack();

            if (!cursorStack.isEmpty()) {
                if (slotStack.isEmpty()) {
                    if (slot.canInsert(cursorStack)) {
                        int amount = clickData == 0 ? cursorStack.getCount() : 1;
                        amount = Math.min(amount, 96);
                        slot.setStack(cursorStack.split(amount));
                        this.sendContentUpdates();
                        return cursorStack;
                    }
                } else if (ItemStack.areItemsEqual(slotStack, cursorStack) && ItemStack.areTagsEqual(slotStack, cursorStack)) {
                    int amount = clickData == 0 ? cursorStack.getCount() : 1;
                    int maxInsert = Math.min(amount, 96 - slotStack.getCount());
                    if (maxInsert > 0) {
                        slotStack.increment(maxInsert);
                        cursorStack.decrement(maxInsert);
                        slot.markDirty();
                        this.sendContentUpdates();
                        return cursorStack;
                    }
                }
            } else if (!slotStack.isEmpty()) {
                int amount = clickData == 0 ? slotStack.getCount() : (slotStack.getCount() + 1) / 2;
                player.inventory.setCursorStack(slot.takeStack(amount));
                this.sendContentUpdates();
                return slot.getStack();
            }
        }
        return super.onSlotClick(slotId, clickData, actionType, player);
    }

    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        boolean bl = false;
        int i = startIndex;
        if (fromLast) {
            i = endIndex - 1;
        }

        Slot slot;
        ItemStack itemStack;
        if (stack.isStackable()) {
            while(!stack.isEmpty() && (!fromLast && i < endIndex || fromLast && i >= startIndex)) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (!itemStack.isEmpty() && itemStack.getItem() == stack.getItem() && ItemStack.areTagsEqual(stack, itemStack)) {
                    int j = itemStack.getCount() + stack.getCount();
                    int maxCount = (i < 3) ? slot.getMaxItemCount() : Math.min(slot.getMaxItemCount(), stack.getMaxCount());

                    if (j <= maxCount) {
                        stack.setCount(0);
                        itemStack.setCount(j);
                        slot.markDirty();
                        bl = true;
                    } else if (itemStack.getCount() < maxCount) {
                        stack.decrement(maxCount - itemStack.getCount());
                        itemStack.setCount(maxCount);
                        slot.markDirty();
                        bl = true;
                    }
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (fromLast) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while(!fromLast && i < endIndex || fromLast && i >= startIndex) {
                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    int maxCount = (i < 3) ? slot.getMaxItemCount() : Math.min(slot.getMaxItemCount(), stack.getMaxCount());
                    if (stack.getCount() > maxCount) {
                        slot.setStack(stack.split(maxCount));
                    } else {
                        slot.setStack(stack.split(stack.getCount()));
                    }

                    slot.markDirty();
                    bl = true;
                    break;
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        return bl;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();

            if (index < 3) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickTransfer(itemStack2, itemStack);
            } else {
                if (AbstractFurnaceBlockEntity.canUseAsFuel(itemStack2)) {
                    if (!this.insertItem(itemStack2, 1, 2, false)) {
                        if (!this.insertItem(itemStack2, 0, 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else {
                    if (!this.insertItem(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }
        return itemStack;
    }

    @Environment(EnvType.CLIENT)
    public int getFuelProgress() {
        int i = this.propertyDelegate.get(1);
        if (i == 0) i = 200;
        return this.propertyDelegate.get(0) * 13 / i;
    }

    @Environment(EnvType.CLIENT)
    public int getCookProgress() {
        int i = this.propertyDelegate.get(2);
        int j = this.propertyDelegate.get(3);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    @Environment(EnvType.CLIENT)
    public boolean isBurning() {
        return this.propertyDelegate.get(0) > 0;
    }
}