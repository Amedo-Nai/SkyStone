package amedonai.ss_celestial_threat.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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

@Mixin(LivingEntity.class)
public class LivingEntityShieldMixin {

    @Inject(method = "damageShield", at = @At("HEAD"), cancellable = true)
    private void skystone$damageModdedShield(float amount, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        ItemStack active = entity.getActiveItem();

        if (active.getItem() instanceof ShieldItem && !active.isOf(Items.SHIELD)) {

            float calculatedAmount = amount;
            if (calculatedAmount < 3.0F) {
                calculatedAmount = 3.0F;
            }

            int damageToApply = 1 + MathHelper.floor(calculatedAmount);
            Hand hand = entity.getActiveHand();

            EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

            active.damage(damageToApply, entity, slot);

            if (active.isEmpty()) {
                entity.equipStack(slot, ItemStack.EMPTY);
                entity.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
            }

            ci.cancel();
        }
    }
}