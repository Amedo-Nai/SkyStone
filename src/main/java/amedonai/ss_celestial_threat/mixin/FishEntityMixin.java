package amedonai.ss_celestial_threat.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import amedonai.ss_celestial_threat.ModItems;

@Mixin(FishEntity.class)
public abstract class FishEntityMixin extends WaterCreatureEntity {

    protected FishEntityMixin(EntityType<? extends WaterCreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    protected abstract void copyDataToStack(ItemStack stack);

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void onInteractMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        Item item = itemStack.getItem();

        boolean isGolden = item == ModItems.GOLDEN_WATER_BUCKET;
        boolean isMeteorite = item == ModItems.METEORITE_IRON_WATER_BUCKET;

        if ((isGolden || isMeteorite) && this.isAlive()) {
            this.playSound(SoundEvents.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            ItemStack fishBucketStack = ItemStack.EMPTY;

            FishEntity fish = (FishEntity) (Object) this;
            if (fish instanceof CodEntity) {
                fishBucketStack = new ItemStack(isGolden ? ModItems.GOLDEN_COD_BUCKET : ModItems.METEORITE_IRON_COD_BUCKET);
            } else if (fish instanceof SalmonEntity) {
                fishBucketStack = new ItemStack(isGolden ? ModItems.GOLDEN_SALMON_BUCKET : ModItems.METEORITE_IRON_SALMON_BUCKET);
            } else if (fish instanceof PufferfishEntity) {
                fishBucketStack = new ItemStack(isGolden ? ModItems.GOLDEN_PUFFERFISH_BUCKET : ModItems.METEORITE_IRON_PUFFERFISH_BUCKET);
            } else if (fish instanceof TropicalFishEntity) {
                fishBucketStack = new ItemStack(isGolden ? ModItems.GOLDEN_TROPICAL_FISH_BUCKET : ModItems.METEORITE_IRON_TROPICAL_FISH_BUCKET);
            }

            if (!fishBucketStack.isEmpty()) {
                this.copyDataToStack(fishBucketStack);

                ItemStack remaining = itemStack.copy();
                remaining.decrement(1);

                if (remaining.isEmpty()) {
                    player.setStackInHand(hand, fishBucketStack);
                } else {
                    player.setStackInHand(hand, remaining);
                    // Исправлено: доступ к инвентарю через getInventory()
                    if (!player.getInventory().insertStack(fishBucketStack)) {
                        player.dropItem(fishBucketStack, false);
                    }
                }

                if (!this.getWorld().isClient) {
                    this.discard();
                }
                cir.setReturnValue(ActionResult.success(this.getWorld().isClient));
            }
        }
    }
}