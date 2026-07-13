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

public class AsteroidFeature extends Feature<AsteroidFeatureConfig> {
    public AsteroidFeature(Codec<AsteroidFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<AsteroidFeatureConfig> context) {
        // Извлекаем все необходимые данные из современного FeatureContext
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos pos = context.getOrigin();
        AsteroidFeatureConfig config = context.getConfig();

        // Случайно распределяем координаты внутри чанка (0-15)
        int randomX = pos.getX() + random.nextInt(16);
        int randomZ = pos.getZ() + random.nextInt(16);

        float outerR = config.outerRadius;
        float innerR = config.innerRadius;

        // 1. Рассчитываем форму и масштабы заранее
        double scaleX = 1.0;
        double scaleY = 1.0;
        double scaleZ = 1.0;

        if (outerR > 2.0f) { // Применяется только для средних, больших и гигантских метеоритов
            scaleX = 1.3 + random.nextDouble() * 0.4; // Растягиваем вширь по X (1.3 - 1.7)
            scaleY = 0.5 + random.nextDouble() * 0.2; // Сильно приплюскиваем по Y (0.5 - 0.7)
            scaleZ = 1.1 + random.nextDouble() * 0.4; // Растягиваем в длину по Z (1.1 - 1.5)
        }

        // Рассчитываем динамические границы циклов на основе искажения осей
        int maxRX = (int) Math.ceil(outerR * scaleX + 1);
        int maxRY = (int) Math.ceil(outerR * scaleY + 1);
        int maxRZ = (int) Math.ceil(outerR * scaleZ + 1);

        BlockPos targetPos;

        if (config.isOceanFloor) {
            // Берем начальную точку по хитмапу
            BlockPos floorPos = world.getTopPosition(Heightmap.Type.OCEAN_FLOOR_WG, new BlockPos(randomX, 0, randomZ));

            // Игнорируем воду, ламинарии и траву. Спускаемся строго до твердого грунта дна.
            while (floorPos.getY() > world.getBottomY()) {
                BlockState state = world.getBlockState(floorPos);
                if (state.isOf(Blocks.STONE) || state.isOf(Blocks.DEEPSLATE) || state.isOf(Blocks.TUFF)
                        || state.isOf(Blocks.SAND) || state.isOf(Blocks.GRAVEL)
                        || state.isOf(Blocks.DIRT) || state.isOf(Blocks.CLAY) || state.isOf(Blocks.ANDESITE)
                        || state.isOf(Blocks.DIORITE) || state.isOf(Blocks.GRANITE)) {
                    break;
                }
                floorPos = floorPos.down();
            }

            // ДИНАМИЧЕСКОЕ УТОПЛЕНИЕ: Прячем центр метеорита в зависимости от его высоты (maxRY)
            int buryDepth = (outerR > 2.0f) ? (maxRY / 2 + 1) : 1;
            targetPos = floorPos.down(buryDepth);

            if (!world.getBiome(targetPos).isIn(BiomeTags.IS_OCEAN)) {
                return false;
            }
        } else {
            // Универсальная начальная высота для подземного сканирования
            int startY = 40 + random.nextInt(20);

            BlockPos.Mutable mutablePos = new BlockPos.Mutable(randomX, startY, randomZ);

            // Спускаемся строго вниз. Наш цикл САМ пропустит воздух пещер и воду океана, пока не найдет твердое дно!
            while (mutablePos.getY() > world.getBottomY()) {
                BlockState state = world.getBlockState(mutablePos);
                if (!state.isAir() && !state.isOf(Blocks.WATER) && !state.isOf(Blocks.LAVA)) {
                    break; // Мы нашли пол пещеры или твердую породу под водой!
                }
                mutablePos.move(0, -1, 0);
            }

            // Если провалились до бедрока и ничего не нашли — отменяем генерацию в этой точке
            if (mutablePos.getY() <= world.getBottomY()) {
                return false;
            }

            // Утапливаем центр метеорита в породу наполовину его высоты
            int buryDepth = (outerR > 2.0f) ? (maxRY / 2) : 1;
            targetPos = mutablePos.down(buryDepth);
        }

        // ЗАЩИТА ОТ НАЛОЖЕНИЯ
        BlockState centerState = world.getBlockState(targetPos);
        if (centerState.isOf(ModBlocks.SKY_STONE) || centerState.isOf(ModBlocks.METEORITE_IRON_ORE)) {
            return false;
        }

        boolean generatedAny = false;

        // --- 2. ЦИКЛ ГЕНЕРАЦИИ ТЕЛА МЕТЕОРИТА ---
        for (int x = -maxRX; x <= maxRX; x++) {
            for (int y = -maxRY; y <= maxRY; y++) {
                for (int z = -maxRZ; z <= maxRZ; z++) {

                    // Эллипсоидная базовая дистанция
                    double distance = Math.sqrt(
                            (x * x) / (scaleX * scaleX) +
                                    (y * y) / (scaleY * scaleY) +
                                    (z * z) / (scaleZ * scaleZ)
                    );

                    // Добавляем неровность формы (бугристость)
                    if (outerR > 2.0f) {
                        distance += Math.sin(x * 1.3) * Math.cos(z * 1.3) * 0.25;
                        distance += Math.cos(y * 1.5) * 0.15;
                    }

                    if (distance <= outerR) {
                        BlockPos currentPos = targetPos.add(x, y, z);
                        BlockState currentState = world.getBlockState(currentPos);

                        if (canReplace(currentState, config.isOceanFloor)) {
                            // Генерация ядра (руды)
                            if (innerR == 0 && x == 0 && y == 0 && z == 0) {
                                world.setBlockState(currentPos, ModBlocks.METEORITE_IRON_ORE.getDefaultState(), 2);
                                generatedAny = true;
                            }
                            else if (innerR > 0 && distance <= innerR) {
                                world.setBlockState(currentPos, ModBlocks.METEORITE_IRON_ORE.getDefaultState(), 2);
                                generatedAny = true;
                            }
                            // Генерация каменной оболочки
                            else {
                                world.setBlockState(currentPos, ModBlocks.SKY_STONE.getDefaultState(), 2);
                                generatedAny = true;
                            }
                        }
                    }
                }
            }
        }

        return generatedAny;
    }

    private boolean canReplace(BlockState state, boolean isOceanFloor) {
        if (state.isAir()) {
            return isOceanFloor; // Если это подземная генерация (false) — воздух заменять НЕЛЬЗЯ!
        }

        if (state.isOf(Blocks.WATER)) {
            return isOceanFloor;
        }

        // Разрушаем водоросли на дне океана
        if (state.isOf(Blocks.SEAGRASS) || state.isOf(Blocks.TALL_SEAGRASS)
                || state.isOf(Blocks.KELP) || state.isOf(Blocks.KELP_PLANT)) {
            return isOceanFloor;
        }

        return state.isOf(Blocks.STONE) || state.isOf(Blocks.DEEPSLATE) || state.isOf(Blocks.TUFF)
                || state.isOf(Blocks.GRANITE) || state.isOf(Blocks.DIORITE) || state.isOf(Blocks.ANDESITE)
                || state.isOf(Blocks.DIRT) || state.isOf(Blocks.SAND) || state.isOf(Blocks.GRAVEL) || state.isOf(Blocks.CLAY)
                || state.isOf(Blocks.COAL_ORE) || state.isOf(Blocks.DEEPSLATE_COAL_ORE)
                || state.isOf(Blocks.IRON_ORE) || state.isOf(Blocks.DEEPSLATE_IRON_ORE)
                || state.isOf(Blocks.GOLD_ORE) || state.isOf(Blocks.DEEPSLATE_GOLD_ORE)
                || state.isOf(Blocks.REDSTONE_ORE) || state.isOf(Blocks.DEEPSLATE_REDSTONE_ORE)
                || state.isOf(Blocks.LAPIS_ORE) || state.isOf(Blocks.DEEPSLATE_LAPIS_ORE)
                || state.isOf(Blocks.DIAMOND_ORE) || state.isOf(Blocks.DEEPSLATE_DIAMOND_ORE)
                || state.isOf(Blocks.COPPER_ORE) || state.isOf(Blocks.DEEPSLATE_COPPER_ORE);
    }
}