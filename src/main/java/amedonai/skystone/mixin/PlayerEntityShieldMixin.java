package amedonai.skystone.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityShieldMixin {

    @Inject(method = "damageShield", at = @At("HEAD"), cancellable = true)
    private void skystone$damageModdedShield(float amount, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        ItemStack active = player.getActiveItem();

        if (active.getItem() instanceof ShieldItem && active.getItem() != Items.SHIELD) {

            float calculatedAmount = amount;
            if (calculatedAmount < 3.0F) {
                calculatedAmount = 3.0F;
            }

            int damageToApply = 1 + MathHelper.floor(calculatedAmount);
            Hand hand = player.getActiveHand();

            active.damage(damageToApply, player, (p) -> p.sendToolBreakStatus(hand));

            if (active.isEmpty()) {
                if (hand == Hand.MAIN_HAND) {
                    player.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                } else {
                    player.equipStack(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + player.world.random.nextFloat() * 0.4F);
            }
            ci.cancel();
        }
    }
}