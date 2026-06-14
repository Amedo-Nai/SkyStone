package vmua.skystone;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.function.Supplier;

public class ModFilledBucketItem extends BucketItem {
    private final Supplier<Item> emptyBucket;

    public ModFilledBucketItem(Fluid fluid, Supplier<Item> emptyBucket, Settings settings) {
        super(fluid, settings);
        this.emptyBucket = emptyBucket;
    }

    @Override
    public ItemStack getEmptiedStack(ItemStack stack, PlayerEntity player) {
        return !player.abilities.creativeMode ? new ItemStack(emptyBucket.get()) : stack;
    }
}