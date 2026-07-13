package amedonai.ss_celestial_threat.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import amedonai.ss_celestial_threat.ModBlocks;

public class CraterFeature extends Feature<CraterFeatureConfig> {
    public CraterFeature(Codec<CraterFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<CraterFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();
        CraterFeatureConfig config = context.getConfig();

        int randomX = pos.getX() + random.nextInt(16);
        int randomZ = pos.getZ() + random.nextInt(16);

        BlockPos surfacePos = world.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(randomX, 0, randomZ));

        var biomeEntry = world.getBiome(surfacePos);
        if (biomeEntry.isIn(BiomeTags.IS_OCEAN) ||
                biomeEntry.isIn(BiomeTags.IS_RIVER) ||
                biomeEntry.isIn(BiomeTags.IS_BEACH)) {
            return false;
        }

        int rX = config.craterRadius;
        int rY = config.craterDepth;
        int rZ = config.craterRadius;

        // Координаты центрального чанка генерации
        int originChunkX = pos.getX() >> 4;
        int originChunkZ = pos.getZ() >> 4;

        // ПРОВЕРКА НА ВОДУ
        for (int x = -rX; x <= rX; x++) {
            for (int z = -rZ; z <= rZ; z++) {
                if ((double) (x * x) / (rX * rX) + (double) (z * z) / (rZ * rZ) <= 1.0) {
                    BlockPos checkPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(randomX + x, 0, randomZ + z));

                    // Защита от выхода за границы региона при проверке воды
                    int checkChunkX = checkPos.getX() >> 4;
                    int checkChunkZ = checkPos.getZ() >> 4;
                    if (Math.abs(checkChunkX - originChunkX) > 1 || Math.abs(checkChunkZ - originChunkZ) > 1) {
                        continue;
                    }

                    if (world.getBlockState(checkPos).isOf(Blocks.WATER) ||
                            world.getBlockState(checkPos.down()).isOf(Blocks.WATER) ||
                            !world.getFluidState(checkPos).isEmpty()) {
                        return false;
                    }
                }
            }
        }

        // ВЫРЕЗАНИЕ ЧАШИ КРАТЕРА
        for (int x = -rX; x <= rX; x++) {
            for (int z = -rZ; z <= rZ; z++) {
                double distSq = (double) (x * x) / (rX * rX) + (double) (z * z) / (rZ * rZ);
                if (distSq <= 1.0) {
                    int depthAtPos = (int) ((1.0 - distSq) * rY);

                    BlockPos columnPos = new BlockPos(randomX + x, 0, randomZ + z);

                    // ГВАРДРЕЙЛ: Проверяем, находится ли блок в пределах безопасной зоны 3х3 чанка
                    int currentChunkX = columnPos.getX() >> 4;
                    int currentChunkZ = columnPos.getZ() >> 4;
                    if (Math.abs(currentChunkX - originChunkX) > 1 || Math.abs(currentChunkZ - originChunkZ) > 1) {
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

        // ГЕНЕРАЦИЯ АСТЕРОИДА НА ДНЕ
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

                        // ГВАРДРЕЙЛ ДЛЯ АСТЕРОИДА
                        int currentChunkX = currentPos.getX() >> 4;
                        int currentChunkZ = currentPos.getZ() >> 4;
                        if (Math.abs(currentChunkX - originChunkX) > 1 || Math.abs(currentChunkZ - originChunkZ) > 1) {
                            continue;
                        }

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