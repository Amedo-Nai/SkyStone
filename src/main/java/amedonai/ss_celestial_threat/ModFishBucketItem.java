package amedonai.ss_celestial_threat;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ModFishBucketItem extends ModFilledBucketItem {
    private final EntityType<?> fishType;

    public ModFishBucketItem(EntityType<?> fishType, Supplier<Item> emptyBucket, Settings settings) {
        super(Fluids.WATER, emptyBucket, settings);
        this.fishType = fishType;
    }

    // 1. Исправленная сигнатура метода (добавлен PlayerEntity)
    @Override
    public void onEmptied(@Nullable PlayerEntity player, World world, ItemStack stack, BlockPos pos) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {
            this.spawnFish(serverWorld, stack, pos);
        }
    }

    private void spawnFish(ServerWorld world, ItemStack stack, BlockPos pos) {
        this.fishType.spawnFromItemStack(world, stack, null, pos, SpawnReason.BUCKET, true, false);
    }
}