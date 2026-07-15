package amedonai.skystone.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import amedonai.skystone.ModBlocks;
import amedonai.skystone.ModEntities;
import amedonai.skystone.entity.MeteoriteIronGolemEntity;

@Mixin(CarvedPumpkinBlock.class)
public class CarvedPumpkinBlockMixin {
    private BlockPattern meteoriteIronGolemPattern;

    @Inject(method = "trySpawnEntity", at = @At("HEAD"), cancellable = true)
    private void trySpawnMeteoriteGolem(World world, BlockPos pos, CallbackInfo ci) {
        BlockPattern.Result result = this.getMeteoriteIronGolemPattern().searchAround(world, pos);
        if (result != null) {

            // 1. Исчезновение блоков структуры
            for (int i = 0; i < this.getMeteoriteIronGolemPattern().getWidth(); ++i) {
                for (int j = 0; j < this.getMeteoriteIronGolemPattern().getHeight(); ++j) {
                    CachedBlockPosition cachedBlockPosition = result.translate(i, j, 0);
                    world.setBlockState(cachedBlockPosition.getBlockPos(), Blocks.AIR.getDefaultState(), 2);
                    world.syncWorldEvent(2001, cachedBlockPosition.getBlockPos(), net.minecraft.block.Block.getRawIdFromState(cachedBlockPosition.getBlockState()));
                }
            }

            // 2. Спавн голема
            BlockPos spawnPos = result.translate(1, 2, 0).getBlockPos();
            MeteoriteIronGolemEntity golem = ModEntities.METEORITE_IRON_GOLEM.create(world);
            if (golem != null) {
                golem.setPlayerCreated(true);
                golem.refreshPositionAndAngles((double)spawnPos.getX() + 0.5D, (double)spawnPos.getY() + 0.05D, (double)spawnPos.getZ() + 0.5D, 0.0F, 0.0F);
                world.spawnEntity(golem);

                for (int i = 0; i < this.getMeteoriteIronGolemPattern().getWidth(); ++i) {
                    for (int j = 0; j < this.getMeteoriteIronGolemPattern().getHeight(); ++j) {
                        CachedBlockPosition cachedBlockPosition2 = result.translate(i, j, 0);
                        world.updateNeighbors(cachedBlockPosition2.getBlockPos(), Blocks.AIR);
                    }
                }

                ci.cancel();
            }
        }
    }

    private BlockPattern getMeteoriteIronGolemPattern() {
        if (this.meteoriteIronGolemPattern == null) {
            this.meteoriteIronGolemPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~")
                    .where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(ModBlocks.METEORITE_IRON_BLOCK)))
                    .where('^', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.CARVED_PUMPKIN).or(BlockStatePredicate.forBlock(Blocks.JACK_O_LANTERN))))
                    .where('~', CachedBlockPosition.matchesBlockState(state -> state.isAir()))
                    .build();
        }
        return this.meteoriteIronGolemPattern;
    }
}