package amedonai.skystone.mixin;

import amedonai.skystone.ModFilledBucketItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {

    @Shadow public abstract void setLevel(World world, BlockPos pos, BlockState state, int level);

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void onUseCustomBucket(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        if (item instanceof ModFilledBucketItem && ((ModFilledBucketItem) item).getFluid() == Fluids.WATER) {
            int currentLevel = state.get(CauldronBlock.LEVEL);

            if (currentLevel < 3) {
                if (!world.isClient) {
                    ItemStack emptyStack = ((ModFilledBucketItem) item).getEmptiedStack(itemStack, player);

                    if (!player.abilities.creativeMode) {
                        itemStack.decrement(1);
                        if (itemStack.isEmpty()) {
                            player.setStackInHand(hand, emptyStack);
                        } else if (!player.inventory.insertStack(emptyStack)) {
                            player.dropItem(emptyStack, false);
                        }
                    }

                    this.setLevel(world, pos, state, 3);
                    player.incrementStat(Stats.FILL_CAULDRON);
                    world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                cir.setReturnValue(ActionResult.success(world.isClient));
            }
        }
    }
}