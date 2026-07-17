package amedonai.skystone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import amedonai.skystone.GolemTracker;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "onPlaced", at = @At("HEAD"))
    private void capturePumpkinPlacer(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if ((Object)this instanceof CarvedPumpkinBlock && placer instanceof ServerPlayerEntity) {
            GolemTracker.PLACER_TRACKER.set((ServerPlayerEntity) placer);
        }
    }

    @Inject(method = "onPlaced", at = @At("TAIL"))
    private void clearPumpkinPlacer(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if ((Object)this instanceof CarvedPumpkinBlock) {
            GolemTracker.PLACER_TRACKER.remove();
        }
    }
}