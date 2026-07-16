package amedonai.skystone;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import java.util.function.Supplier;

public class ModMilkBucketItem extends Item {
    private final Supplier<Item> emptyBucket;

    public ModMilkBucketItem(Supplier<Item> emptyBucket, Settings settings) {
        super(settings);
        this.emptyBucket = emptyBucket;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient) {
            // Очищаем все эффекты с сущности
            user.clearStatusEffects();
        }

        // Если пьет игрок и он не в креативе — уменьшаем стак
        if (user instanceof PlayerEntity && !((PlayerEntity) user).abilities.creativeMode) {
            stack.decrement(1);
        }

        if (user instanceof PlayerEntity) {
            ((PlayerEntity) user).incrementStat(Stats.USED.getOrCreateStat(this));
        }

        return stack.isEmpty() ? new ItemStack(this.emptyBucket.get()) : stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32; // Стандартное время питья/еды в майнкрафте (1.6 секунды)
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK; // Запускает анимацию и звук питья
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Позволяет начать использование предмета по нажатию ПКМ
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}