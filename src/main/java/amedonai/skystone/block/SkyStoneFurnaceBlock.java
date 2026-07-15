package amedonai.skystone.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import amedonai.skystone.TooltipHelper;
import amedonai.skystone.entity.SkyStoneFurnaceBlockEntity;
import amedonai.skystone.ModParticles;

import java.util.List;
import java.util.Random;

public class SkyStoneFurnaceBlock extends AbstractFurnaceBlock {
    public SkyStoneFurnaceBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NamedScreenHandlerFactory) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new SkyStoneFurnaceBlockEntity();
    }

    // Выпадение содержимого при ломании печки
    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof SkyStoneFurnaceBlockEntity) {
                // Выбрасываем всё содержимое инвентаря в мир вокруг блока
                ItemScatterer.spawn(world, pos, (SkyStoneFurnaceBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }

            // Вызываем супер-метод для очистки BlockEntity из мира
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY();
            double z = (double)pos.getZ() + 0.5D;

            if (random.nextDouble() < 0.1D) {
                world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double offsetFactor = 0.52D;
            double randomOffset = random.nextDouble() * 0.6D - 0.3D;

            double offsetX = axis == Direction.Axis.X ? (double)direction.getOffsetX() * offsetFactor : randomOffset;
            double offsetY = random.nextDouble() * 6.0D / 16.0D;
            double offsetZ = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * offsetFactor : randomOffset;

            world.addParticle(ParticleTypes.SMOKE, x + offsetX, y + offsetY, z + offsetZ, 0.0D, 0.0D, 0.0D);
            world.addParticle(ModParticles.SKY_STONE_FLAME, x + offsetX, y + offsetY, z + offsetZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
        super.appendTooltip(stack, world, tooltip, options);
    }
}