package amedonai.skystone.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import amedonai.skystone.ModBlocks;

import java.util.Random;

public class CraterFeature extends Feature<CraterFeatureConfig> {
    public CraterFeature(Codec<CraterFeatureConfig> configCodec) {
        super(configCodec);
    }

    // Метод защиты от каскадной генерации чанков
    private boolean isSafe(StructureWorldAccess world, BlockPos pos) {
        if (world instanceof ChunkRegion) {
            ChunkRegion region = (ChunkRegion) world;
            int centerX = region.getCenterChunkX();
            int centerZ = region.getCenterChunkZ();
            int chunkX = pos.getX() >> 4;
            int chunkZ = pos.getZ() >> 4;
            return Math.abs(chunkX - centerX) <= 1 && Math.abs(chunkZ - centerZ) <= 1;
        }
        return true;
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, CraterFeatureConfig config) {
        int randomX = pos.getX() + random.nextInt(16);
        int randomZ = pos.getZ() + random.nextInt(16);

        BlockPos surfacePos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(randomX, 0, randomZ));

        // Проверка биома в центре
        Biome biome = world.getBiome(surfacePos);
        if (biome.getCategory() == Biome.Category.OCEAN ||
                biome.getCategory() == Biome.Category.RIVER ||
                biome.getCategory() == Biome.Category.BEACH) {
            return false;
        }

        int rX = config.craterRadius;
        int rY = config.craterDepth;
        int rZ = config.craterRadius;

        // Сканирование на воду
        for (int x = -rX; x <= rX; x++) {
            for (int z = -rZ; z <= rZ; z++) {
                if ((double) (x * x) / (rX * rX) + (double) (z * z) / (rZ * rZ) <= 1.0) {
                    BlockPos colPos = new BlockPos(randomX + x, 0, randomZ + z);

                    if (!isSafe(world, colPos)) {
                        continue;
                    }

                    BlockPos checkPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, colPos);

                    if (world.getBlockState(checkPos).isOf(Blocks.WATER) ||
                            world.getBlockState(checkPos.down()).isOf(Blocks.WATER) ||
                            !world.getFluidState(checkPos).isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // Вырезание пустоты кратера
        for (int x = -rX; x <= rX; x++) {
            for (int z = -rZ; z <= rZ; z++) {
                double distSq = (double) (x * x) / (rX * rX) + (double) (z * z) / (rZ * rZ);
                if (distSq <= 1.0) {
                    int depthAtPos = (int) ((1.0 - distSq) * rY);

                    BlockPos columnPos = new BlockPos(randomX + x, 0, randomZ + z);

                    if (!isSafe(world, columnPos)) {
                        continue;
                    }

                    int topY = world.getTopPosition(Heightmap.Type.WORLD_SURFACE, columnPos).getY();
                    int maxClearY = Math.max(surfacePos.getY() + 30, topY);

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

        // Динамический расчет параметров астероида
        float outerR = 0.0f;
        float innerR = 0.0f;
        boolean shouldGenerateAsteroid = true;

        if (rX <= 10) {
            // Маленький кратер (SURFACE_CRATER_MEDIUM, радиус 9)
            // Шанс 50/50: либо маленькая залежа, либо вообще ничего
            if (random.nextBoolean()) {
                outerR = 1.3f;
                innerR = 0.0f;
            } else {
                shouldGenerateAsteroid = false;
            }
        } else if (rX <= 18) {
            // Средний кратер (SURFACE_CRATER_LARGE, радиус 15)
            // Всегда метеорит, но 50/50 либо маленькая залежа, либо средняя
            if (random.nextBoolean()) {
                outerR = 1.3f;
                innerR = 0.0f;
            } else {
                outerR = 2.2f;
                innerR = 1.0f;
            }
        } else {
            // Гигантский кратер (SURFACE_CRATER_GIANT, радиус 24)
            // Всегда средняя залежа
            outerR = 2.2f;
            innerR = 1.0f;
        }

        // Генерация астероида на дне (если прошел проверку шанса)
        if (shouldGenerateAsteroid && outerR > 0.0f) {
            BlockPos asteroidCenter = surfacePos.down(rY - 1);

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

                            if (!isSafe(world, currentPos)) {
                                continue;
                            }

                            BlockState currentState = world.getBlockState(currentPos);

                            if (currentState.isAir() || canReplaceForAsteroid(currentState)) {
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
        }

        return true;
    }

    private boolean canReplaceWithAir(BlockState state) {

        return !state.isOf(Blocks.BEDROCK);
    }

    private boolean canReplaceForAsteroid(BlockState state) {
        return state.isOf(Blocks.STONE) || state.isOf(Blocks.GRANITE) || state.isOf(Blocks.DIORITE)
                || state.isOf(Blocks.ANDESITE) || state.isOf(Blocks.DIRT) || state.isOf(Blocks.SAND)
                || state.isOf(Blocks.GRAVEL) || state.isOf(Blocks.CLAY) || state.isOf(Blocks.GRASS_BLOCK)
                || state.isOf(Blocks.COAL_ORE) || state.isOf(Blocks.IRON_ORE);
    }
}