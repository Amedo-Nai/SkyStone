package vmua.skystone.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import vmua.skystone.ModBlocks;

import java.util.Random;

public class CraterFeature extends Feature<CraterFeatureConfig> {
    public CraterFeature(Codec<CraterFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, CraterFeatureConfig config) {
        int randomX = pos.getX() + random.nextInt(16);
        int randomZ = pos.getZ() + random.nextInt(16);

        // Находим реальную поверхность земли для центра кратера
        BlockPos surfacePos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(randomX, 0, randomZ));
        // 1. ПРОВЕРКА БИОМА В ЦЕНТРЕ (Исключаем океаны, реки и пляжи)
        Biome biome = world.getBiome(surfacePos);
        if (biome.getCategory() == Biome.Category.OCEAN ||
                biome.getCategory() == Biome.Category.RIVER ||
                biome.getCategory() == Biome.Category.BEACH) {
            return false;
        }

        int rX = config.craterRadius;
        int rY = config.craterDepth;
        int rZ = config.craterRadius;

        // 2. СКАНИРОВАНИЕ ВСЕЙ ПЛОЩАДИ КРАТЕРА НА ВОДУ (С учётом реального рельефа)
        for (int x = -rX; x <= rX; x++) {
            for (int z = -rZ; z <= rZ; z++) {
                if ((double) (x * x) / (rX * rX) + (double) (z * z) / (rZ * rZ) <= 1.0) {
                    // Находим РЕАЛЬНУЮ поверхность для каждой координаты круга
                    BlockPos checkPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(randomX + x, 0, randomZ + z));

                    if (world.getBlockState(checkPos).isOf(Blocks.WATER) ||
                            world.getBlockState(checkPos.down()).isOf(Blocks.WATER) ||
                            !world.getFluidState(checkPos).isEmpty()) {
                        return false; // Нашли воду на любой высоте в пределах круга — отменяем
                    }
                }
            }
        }

        // 3. ВЫРЕЗАНИЕ ЧИСТОЙ ПУСТОТЫ (Убираем вертикальные скалы)
        for (int x = -rX; x <= rX; x++) {
            for (int z = -rZ; z <= rZ; z++) {
                double distSq = (double) (x * x) / (rX * rX) + (double) (z * z) / (rZ * rZ);
                if (distSq <= 1.0) {
                    int depthAtPos = (int) ((1.0 - distSq) * rY);

                    // Находим самую высокую точку рельефа в этой координате, чтобы снести склон горы над кратером
                    BlockPos columnPos = new BlockPos(randomX + x, 0, randomZ + z);
                    int topY = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, columnPos).getY();
                    int maxClearY = Math.max(surfacePos.getY() + 30, topY);

                    // Чистим от дна чаши до самого верха горы
                    for (int y = -depthAtPos; y <= (maxClearY - surfacePos.getY()); y++) {
                        BlockPos currentPos = surfacePos.add(x, y, z);
                        BlockState state = world.getBlockState(currentPos);
                        if (canReplaceWithAir(state)) {
                            world.setBlockState(currentPos, Blocks.AIR.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        // 4. ГЕНЕРАЦИЯ АСТЕРОИДНОГО ЗАЛЕЖА НА ДНЕ ПУСТОТЫ
        BlockPos asteroidCenter = surfacePos.down(rY - 1);

        float outerR = config.asteroidOuterRadius;
        float innerR = config.asteroidInnerRadius;

        double scaleX = 0.8 + random.nextDouble() * 0.2;
        double scaleY = 0.5 + random.nextDouble() * 0.2;
        double scaleZ = 0.7 + random.nextDouble() * 0.2;

        int maxRX = (int) Math.ceil(outerR * scaleX + 1);
        int maxRY = (int) Math.ceil(outerR * scaleY + 1);
        int maxRZ = (int) Math.ceil(outerR * scaleZ + 1);

        for (int x = -maxRX; x <= maxRX; x++) {
            for (int y = -maxRY; y <= maxRY; y++) {
                for (int z = -maxRZ; z <= maxRZ; z++) {

                    double distance = Math.sqrt(
                            (x * x) / (scaleX * scaleX) +
                                    (y * y) / (scaleY * scaleY) +
                                    (z * z) / (scaleZ * scaleZ)
                    );

                    distance += Math.sin(x * 1.3) * Math.cos(z * 1.3) * 0.2;

                    if (distance <= outerR) {
                        BlockPos currentPos = asteroidCenter.add(x, y, z);
                        BlockState currentState = world.getBlockState(currentPos);

                        if (currentState.isAir()) {
                            if ((innerR > 0 && distance <= innerR) || (x == 0 && y == 0 && z == 0)) {
                                world.setBlockState(currentPos, ModBlocks.METEORITE_IRON_ORE.getDefaultState(), 2);
                            } else {
                                world.setBlockState(currentPos, ModBlocks.SKY_STONE.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean canReplaceWithAir(BlockState state) {
        return !state.isOf(Blocks.BEDROCK);
    }
}