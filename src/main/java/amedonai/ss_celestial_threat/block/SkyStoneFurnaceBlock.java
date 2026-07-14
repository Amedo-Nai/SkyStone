package amedonai.ss_celestial_threat.block;

import amedonai.ss_celestial_threat.ModBlockEntities;
import amedonai.ss_celestial_threat.ModParticles;
import amedonai.ss_celestial_threat.TooltipHelper;
import amedonai.ss_celestial_threat.entity.SkyStoneFurnaceBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class SkyStoneFurnaceBlock extends AbstractFurnaceBlock {
    public static final MapCodec<SkyStoneFurnaceBlock> CODEC = createCodec(SkyStoneFurnaceBlock::new);

    public SkyStoneFurnaceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public MapCodec<SkyStoneFurnaceBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SkyStoneFurnaceBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
        }
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SkyStoneFurnaceBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, ModBlockEntities.SKY_STONE_FURNACE_ENTITY, SkyStoneFurnaceBlockEntity::tick);
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof SkyStoneFurnaceBlockEntity) {
                ItemScatterer.spawn(world, pos, (SkyStoneFurnaceBlockEntity) blockEntity);
                world.updateComparators(pos, this);
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
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
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
        super.appendTooltip(stack, context, tooltip, type);
    }
}