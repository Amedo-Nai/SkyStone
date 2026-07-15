package amedonai.skystone;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.function.Supplier;

public class ModFishBucketItem extends ModFilledBucketItem {
    private final EntityType<?> fishType;

    public ModFishBucketItem(EntityType<?> fishType, Supplier<Item> emptyBucket, Settings settings) {
        super(Fluids.WATER, emptyBucket, settings);
        this.fishType = fishType;
    }

    @Override
    public void onEmptied(World world, ItemStack stack, BlockPos pos) {
        if (!world.isClient && world instanceof ServerWorld) {
            this.spawnFish((ServerWorld) world, stack, pos);
        }
    }

    private void spawnFish(ServerWorld world, ItemStack stack, BlockPos pos) {
        Entity entity = this.fishType.spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
        if (entity instanceof FishEntity) {
            ((FishEntity) entity).setFromBucket(true);
        }

        if (entity instanceof TropicalFishEntity) {
            NbtCompound nbtCompound = stack.getTag();
            if (nbtCompound != null && nbtCompound.contains("BucketVariantTag", 3)) {
                ((TropicalFishEntity) entity).setVariant(nbtCompound.getInt("BucketVariantTag"));
            }
        }
    }
}