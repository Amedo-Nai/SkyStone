package vmua.skystone;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import java.util.function.Supplier;
import net.minecraft.block.Waterloggable;
import net.minecraft.state.property.Properties;

public class ModEmptyBucketItem extends Item {
    private final Supplier<Item> waterBucket;
    private final Supplier<Item> lavaBucket;

    public ModEmptyBucketItem(Supplier<Item> waterBucket, Supplier<Item> lavaBucket, Settings settings) {
        super(settings);
        this.waterBucket = waterBucket;
        this.lavaBucket = lavaBucket;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemStack);
        }

        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos offsetPos = blockPos.offset(direction);

            if (!world.canPlayerModifyAt(user, blockPos) || !user.canPlaceOn(offsetPos, direction, itemStack)) {
                return TypedActionResult.fail(itemStack);
            }

            net.minecraft.block.BlockState blockState = world.getBlockState(blockPos);
            FluidState fluidState = world.getFluidState(blockPos);

            // Набираем воду
            if (fluidState.getFluid() == Fluids.WATER && fluidState.isStill()) {
                // Проверяем, является ли блок waterloggable (например, наши прутья)
                if (blockState.getBlock() instanceof Waterloggable && blockState.get(Properties.WATERLOGGED)) {
                    world.setBlockState(blockPos, blockState.with(Properties.WATERLOGGED, false), 11);
                } else {
                    world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
                }
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                user.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                ItemStack filled = exchangeStack(itemStack, user, new ItemStack(waterBucket.get()));
                return TypedActionResult.success(filled, world.isClient());
            }
            // Набираем лаву
            else if (fluidState.getFluid() == Fluids.LAVA && fluidState.isStill()) {
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                user.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
                ItemStack filled = exchangeStack(itemStack, user, new ItemStack(lavaBucket.get()));
                return TypedActionResult.success(filled, world.isClient());
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    // Наш собственный фолбэк-метод обмена стаков для 1.16.5
    private ItemStack exchangeStack(ItemStack inputStack, PlayerEntity player, ItemStack outputStack) {
        if (player.abilities.creativeMode) {
            return inputStack;
        } else {
            inputStack.decrement(1);
            if (inputStack.isEmpty()) {
                return outputStack;
            } else {
                if (!player.inventory.insertStack(outputStack)) {
                    player.dropItem(outputStack, false);
                }
                return inputStack;
            }
        }
    }
}