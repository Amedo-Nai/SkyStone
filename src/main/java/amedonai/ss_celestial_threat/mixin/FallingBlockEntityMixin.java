package amedonai.ss_celestial_threat.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import amedonai.ss_celestial_threat.ModBlocks;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {

    @Shadow
    private BlockState block;

    @ModifyConstant(method = "handleFallDamage", constant = @Constant(floatValue = 0.05F))
    private float modifyAnvilDamageChance(float original) {
        Block b = this.block.getBlock();

        if (b == ModBlocks.METEORITE_IRON_ANVIL || b == ModBlocks.CHIPPED_METEORITE_IRON_ANVIL || b == ModBlocks.DAMAGED_METEORITE_IRON_ANVIL) {
            return original * 0.5F;
        }

        if (b == ModBlocks.GOLDEN_ANVIL || b == ModBlocks.CHIPPED_GOLDEN_ANVIL || b == ModBlocks.DAMAGED_GOLDEN_ANVIL) {
            return original * 1.5F;
        }

        return original;
    }
}