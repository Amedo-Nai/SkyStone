package amedonai.ss_celestial_threat;

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
            // Очищаем все эффекты с сущности (как обычное ведро молока)
            user.clearStatusEffects();
        }

        // Проверяем на креатив через метод isCreative()
        if (user instanceof PlayerEntity player && !player.isCreative()) {
            stack.decrement(1);
        }

        if (user instanceof PlayerEntity player) {
            player.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        // Если ведро опустело, возвращаем кастомное пустое ведро
        return stack.isEmpty() ? new ItemStack(this.emptyBucket.get()) : stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 32;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}