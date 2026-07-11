package amedonai.ss_celestial_threat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import java.util.function.Supplier;

public class ModFilledBucketItem extends BucketItem {
    private final Supplier<Item> emptyBucket;

    public ModFilledBucketItem(Fluid fluid, Supplier<Item> emptyBucket, Settings settings) {
        super(fluid, settings);
        this.emptyBucket = emptyBucket;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Вызываем ванильную логику выливания жидкости (звуки, партиклы, размещение флюида)
        TypedActionResult<ItemStack> result = super.use(world, user, hand);

        // Если действие успешно завершилось (жидкость вылилась) и игрок не в креативе
        if (result.getResult().isAccepted() && !user.getAbilities().creativeMode) {
            // Возвращаем руку игрока с твоим кастомным пустым ведром вместо ванильного
            return TypedActionResult.success(new ItemStack(emptyBucket.get()));
        }

        return result;
    }
}