package amedonai.ss_celestial_threat.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import amedonai.ss_celestial_threat.entity.SkyStoneFurnaceBlockEntity;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @Inject(
            method = "isInventoryFull",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void injectIsSkyStoneFurnaceFull(Inventory inventory, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (inventory instanceof SkyStoneFurnaceBlockEntity) {
            SkyStoneFurnaceBlockEntity furnace = (SkyStoneFurnaceBlockEntity) inventory;
            int[] slots = furnace.getAvailableSlots(direction);

            for (int slot : slots) {
                ItemStack stack = furnace.getStack(slot);
                if (stack.isEmpty() || stack.getCount() < furnace.getMaxCountPerStack()) {
                    cir.setReturnValue(false);
                    return;
                }
            }
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void injectSkyStoneFurnaceTransfer(Inventory from, Inventory to, ItemStack stack, int slot, Direction side, CallbackInfoReturnable<ItemStack> cir) {
        if (to instanceof SkyStoneFurnaceBlockEntity) {
            SkyStoneFurnaceBlockEntity furnace = (SkyStoneFurnaceBlockEntity) to;

            if (furnace.canInsert(slot, stack, side)) {
                ItemStack targetStack = furnace.getStack(slot);
                int maxFurnaceStack = furnace.getMaxCountPerStack(); // 96 предметов

                if (targetStack.isEmpty()) {
                    int transferAmount = Math.min(stack.getCount(), maxFurnaceStack);
                    furnace.setStack(slot, stack.split(transferAmount));
                    furnace.markDirty();
                    cir.setReturnValue(stack);
                    return;
                }
                else if (ItemStack.areItemsAndComponentsEqual(targetStack, stack)) {
                    int currentCount = targetStack.getCount();
                    int spaceLeft = maxFurnaceStack - currentCount; // Считаем свободное место до лимита 96

                    int transferAmount = Math.min(stack.getCount(), spaceLeft);
                    if (transferAmount > 0) {
                        targetStack.increment(transferAmount);
                        stack.decrement(transferAmount);
                        furnace.markDirty();
                    }
                    cir.setReturnValue(stack);
                    return;
                }
            }
        }
    }
}