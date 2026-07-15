package amedonai.skystone.world.feature;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import amedonai.skystone.SkyStone;

public class ModFeatures {
    public static final Feature<AsteroidFeatureConfig> ASTEROID = new AsteroidFeature(AsteroidFeatureConfig.CODEC);
    public static final Feature<CraterFeatureConfig> CRATER = new CraterFeature(CraterFeatureConfig.CODEC);

    // ПОДЗЕМНЫЕ
    public static ConfiguredFeature<?, ?> UNDERGROUND_SMALL;
    public static ConfiguredFeature<?, ?> UNDERGROUND_MEDIUM;
    public static ConfiguredFeature<?, ?> UNDERGROUND_LARGE;
    public static ConfiguredFeature<?, ?> UNDERGROUND_GIANT;

    // ОКЕАНИЧЕСКИЕ
    public static ConfiguredFeature<?, ?> OCEAN_SMALL;
    public static ConfiguredFeature<?, ?> OCEAN_MEDIUM;
    public static ConfiguredFeature<?, ?> OCEAN_LARGE;
    public static ConfiguredFeature<?, ?> OCEAN_GIANT;

    public static ConfiguredFeature<?, ?> SURFACE_CRATER_MEDIUM;
    public static ConfiguredFeature<?, ?> SURFACE_CRATER_LARGE;
    public static ConfiguredFeature<?, ?> SURFACE_CRATER_GIANT;

    public static void register() {
        Registry.register(Registry.FEATURE, new Identifier(SkyStone.MOD_ID, "asteroid"), ASTEROID);
        Registry.register(Registry.FEATURE, new Identifier(SkyStone.MOD_ID, "crater"), CRATER);

        // --- НАСТРОЙКИ ПОДЗЕМЕЛЬЯ ---
        UNDERGROUND_SMALL = ASTEROID.configure(new AsteroidFeatureConfig(1.3f, 0.0f, false))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(4)));
        UNDERGROUND_MEDIUM = ASTEROID.configure(new AsteroidFeatureConfig(2.2f, 1.0f, false))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(15)));
        UNDERGROUND_LARGE = ASTEROID.configure(new AsteroidFeatureConfig(3.2f, 1.5f, false))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(45)));
        UNDERGROUND_GIANT = ASTEROID.configure(new AsteroidFeatureConfig(4.2f, 2.2f, false))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(120)));

        // --- НАСТРОЙКИ ОКЕАНА ---
        OCEAN_SMALL = ASTEROID.configure(new AsteroidFeatureConfig(1.3f, 0.0f, true))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(300)));
        OCEAN_MEDIUM = ASTEROID.configure(new AsteroidFeatureConfig(2.2f, 1.0f, true))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(600)));
        OCEAN_LARGE = ASTEROID.configure(new AsteroidFeatureConfig(3.2f, 1.5f, true))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(1200)));
        OCEAN_GIANT = ASTEROID.configure(new AsteroidFeatureConfig(4.3f, 2.3f, true))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(4800)));

        // --- НАСТРОЙКИ ПОВЕРХНОСТНЫХ КРАТЕРОВ (Сбалансированные) ---
        // Параметры: (Радиус кратера, Глубина кратера, Внешний радиус метеорита, Внутреннее рудное ядро)

        // Средний кратер -> Внутри Маленький метеорит (1.3f, 0.0f)
        SURFACE_CRATER_MEDIUM = CRATER.configure(new CraterFeatureConfig(9, 5, 1.3f, 0.0f))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(400)));

        // Большой кратер -> Внутри Средний метеорит (2.2f, 1.0f)
        SURFACE_CRATER_LARGE = CRATER.configure(new CraterFeatureConfig(15, 8, 2.2f, 1.0f))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(800)));

        // Гигантский кратер -> Внутри ТОЖЕ Средний метеорит (2.2f, 1.0f), чтобы не перенасыщать мир рудой
        SURFACE_CRATER_GIANT = CRATER.configure(new CraterFeatureConfig(24, 12, 2.2f, 1.0f))
                .decorate(Decorator.CHANCE.configure(new ChanceDecoratorConfig(2000)));

        // --- РЕГИСТРАЦИЯ ПОДЗЕМЕЛЬЯ ---
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "underground_small"), UNDERGROUND_SMALL);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "underground_medium"), UNDERGROUND_MEDIUM); // Безопасный фоллбэк под твою структуру
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "underground_large"), UNDERGROUND_LARGE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "underground_giant"), UNDERGROUND_GIANT);

        // --- РЕГИСТРАЦИЯ ОКЕАНА ---
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "ocean_small"), OCEAN_SMALL);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "ocean_medium"), OCEAN_MEDIUM);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "ocean_large"), OCEAN_LARGE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "ocean_giant"), OCEAN_GIANT);

        // --- РЕГИСТРАЦИЯ КРАТЕРОВ ---
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "surface_crater_medium"), SURFACE_CRATER_MEDIUM);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "surface_crater_large"), SURFACE_CRATER_LARGE);
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new Identifier(SkyStone.MOD_ID, "surface_crater_giant"), SURFACE_CRATER_GIANT);
    }
}