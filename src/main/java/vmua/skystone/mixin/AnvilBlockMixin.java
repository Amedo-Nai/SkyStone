package vmua.skystone.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vmua.skystone.ModBlocks;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {

    @Inject(method = "getLandingState", at = @At("HEAD"), cancellable = true)
    private static void onGetLandingState(BlockState fallingState, CallbackInfoReturnable<BlockState> cir) {
        Block block = fallingState.getBlock();

        // Золотая наковальня
        if (block == ModBlocks.GOLDEN_ANVIL) {
            cir.setReturnValue(ModBlocks.CHIPPED_GOLDEN_ANVIL.getDefaultState()
                    .with(AnvilBlock.FACING, fallingState.get(AnvilBlock.FACING)));
            return;
        }
        if (block == ModBlocks.CHIPPED_GOLDEN_ANVIL) {
            cir.setReturnValue(ModBlocks.DAMAGED_GOLDEN_ANVIL.getDefaultState()
                    .with(AnvilBlock.FACING, fallingState.get(AnvilBlock.FACING)));
            return;
        }
        if (block == ModBlocks.DAMAGED_GOLDEN_ANVIL) {
            cir.setReturnValue(null);
            return;
        }

        // Метеоритная наковальня (Защита от износа 50% для всей линейки блоков)
        if (block == ModBlocks.METEORITE_IRON_ANVIL || block == ModBlocks.CHIPPED_METEORITE_IRON_ANVIL || block == ModBlocks.DAMAGED_METEORITE_IRON_ANVIL) {
            // Бросаем монетку. Если повезло — возвращаем исходное состояние блока, отменяя урон
            if (ThreadLocalRandom.current().nextFloat() < 0.50F) {
                cir.setReturnValue(fallingState);
                return;
            }

            if (block == ModBlocks.METEORITE_IRON_ANVIL) {
                cir.setReturnValue(ModBlocks.CHIPPED_METEORITE_IRON_ANVIL.getDefaultState()
                        .with(AnvilBlock.FACING, fallingState.get(AnvilBlock.FACING)));
                return;
            }
            if (block == ModBlocks.CHIPPED_METEORITE_IRON_ANVIL) {
                cir.setReturnValue(ModBlocks.DAMAGED_METEORITE_IRON_ANVIL.getDefaultState()
                        .with(AnvilBlock.FACING, fallingState.get(AnvilBlock.FACING)));
                return;
            }
            if (block == ModBlocks.DAMAGED_METEORITE_IRON_ANVIL) {
                cir.setReturnValue(null);
                return;
            }
        }
    }
}