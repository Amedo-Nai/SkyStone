package vmua.skystone.block;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import vmua.skystone.TooltipHelper;
import vmua.skystone.entity.SkyStoneFurnaceBlockEntity;
import vmua.skystone.ModParticles;

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

    // Этот метод вызывается игрой случайным образом на клиенте вокруг блока
    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        // Проверяем, горит ли сейчас печь (LIT = true)
        if (state.get(LIT)) {
            double x = (double)pos.getX() + 0.5D;
            double y = (double)pos.getY();
            double z = (double)pos.getZ() + 0.5D;

            // Звук потрескивания печи
            if (random.nextDouble() < 0.1D) {
                world.playSound(x, y, z, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            // Вычисляем, куда «смотрит» печка, чтобы частицы вылетали из лицевой панели
            Direction direction = state.get(FACING);
            Direction.Axis axis = direction.getAxis();
            double offsetFactor = 0.52D;
            double randomOffset = random.nextDouble() * 0.6D - 0.3D;

            double offsetX = axis == Direction.Axis.X ? (double)direction.getOffsetX() * offsetFactor : randomOffset;
            double offsetY = random.nextDouble() * 6.0D / 16.0D;
            double offsetZ = axis == Direction.Axis.Z ? (double)direction.getOffsetZ() * offsetFactor : randomOffset;

            // Спавним стандартный дым печки, чтобы была атмосфера горения
            world.addParticle(ParticleTypes.SMOKE, x + offsetX, y + offsetY, z + offsetZ, 0.0D, 0.0D, 0.0D);

            // Спавним кастомный фиолетовый/космический огонь вместо ванильного пламени!
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