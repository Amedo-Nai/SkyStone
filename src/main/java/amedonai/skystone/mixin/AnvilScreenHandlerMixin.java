package amedonai.skystone.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import amedonai.skystone.ModBlocks;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    @Shadow
    private net.minecraft.screen.Property levelCost;

    protected AnvilScreenHandlerMixin(net.minecraft.screen.ScreenHandlerType<?> type, int syncId, net.minecraft.entity.player.PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At("RETURN"))
    private void applyGoldenAnvilDiscount(CallbackInfo ci) {
        int currentCost = this.levelCost.get();
        if (currentCost <= 0) {
            return;
        }

        this.context.run((world, blockPos) -> {
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();
            if (block == ModBlocks.GOLDEN_ANVIL || block == ModBlocks.CHIPPED_GOLDEN_ANVIL || block == ModBlocks.DAMAGED_GOLDEN_ANVIL) {
                int discounted = Math.max(1, (int) Math.ceil(currentCost * 0.5));
                this.levelCost.set(discounted);
            }
        });
    }

    @Inject(method = "onTakeOutput", at = @At("HEAD"))
    private void handleAnvilDurabilityOnUse(PlayerEntity player, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (player.abilities.creativeMode) return;

        this.context.run((world, blockPos) -> {
            if (world.isClient) return;

            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            // Золотая наковальня ломается на 50% чаще при использовании
            if (block == ModBlocks.GOLDEN_ANVIL || block == ModBlocks.CHIPPED_GOLDEN_ANVIL || block == ModBlocks.DAMAGED_GOLDEN_ANVIL) {
                if (world.random.nextFloat() < 0.0682F) {
                    triggerAnvilDegrade(world, blockPos, blockState);
                }
            }
        });
    }

    private void triggerAnvilDegrade(World world, BlockPos pos, BlockState state) {
        BlockState nextState = AnvilBlock.getLandingState(state);
        if (nextState == null) {
            world.removeBlock(pos, false);
            world.syncWorldEvent(1029, pos, 0); // Звук разрушения в дребезги
        } else {
            world.setBlockState(pos, nextState, 2);
            world.syncWorldEvent(1030, pos, 0); // Звук износа металла
        }
    }
}