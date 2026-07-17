package amedonai.skystone.mixin;

import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import amedonai.skystone.GolemTracker;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "place", at = @At("HEAD"))
    private void capturePumpkinPlacer(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        // Используем классический instanceof и явное приведение типов
        if (context.getPlayer() instanceof ServerPlayerEntity && context.getStack().getItem() instanceof BlockItem) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) context.getPlayer();
            BlockItem blockItem = (BlockItem) context.getStack().getItem();

            if (blockItem.getBlock() instanceof CarvedPumpkinBlock) {
                GolemTracker.PLACER_TRACKER.set(serverPlayer);
            }
        }
    }

    @Inject(method = "place", at = @At("RETURN"))
    private void clearPumpkinPlacer(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (context.getStack().getItem() instanceof BlockItem) {
            BlockItem blockItem = (BlockItem) context.getStack().getItem();

            if (blockItem.getBlock() instanceof CarvedPumpkinBlock) {
                GolemTracker.PLACER_TRACKER.remove();
            }
        }
    }
}