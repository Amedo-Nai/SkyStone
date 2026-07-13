package amedonai.ss_celestial_threat.world.feature;

import amedonai.ss_celestial_threat.SkyStoneCelestialThreat;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.RarityFilterPlacementModifier; // Новый импорт!

import java.util.List;

public class ModFeatures {
    // 1. Регистрируем базовые фичи
    public static final Feature<AsteroidFeatureConfig> ASTEROID = new AsteroidFeature(AsteroidFeatureConfig.CODEC);
    public static final Feature<CraterFeatureConfig> CRATER = new CraterFeature(CraterFeatureConfig.CODEC);

    // 2. Объявляем Ключи для Конфигураций (Configured Features)
    public static final RegistryKey<ConfiguredFeature<?, ?>> UNDERGROUND_SMALL_CF = cfKey("underground_small");
    public static final RegistryKey<ConfiguredFeature<?, ?>> UNDERGROUND_MEDIUM_CF = cfKey("underground_medium");
    public static final RegistryKey<ConfiguredFeature<?, ?>> UNDERGROUND_LARGE_CF = cfKey("underground_large");
    public static final RegistryKey<ConfiguredFeature<?, ?>> UNDERGROUND_GIANT_CF = cfKey("underground_giant");

    public static final RegistryKey<ConfiguredFeature<?, ?>> OCEAN_SMALL_CF = cfKey("ocean_small");
    public static final RegistryKey<ConfiguredFeature<?, ?>> OCEAN_MEDIUM_CF = cfKey("ocean_medium");
    public static final RegistryKey<ConfiguredFeature<?, ?>> OCEAN_LARGE_CF = cfKey("ocean_large");
    public static final RegistryKey<ConfiguredFeature<?, ?>> OCEAN_GIANT_CF = cfKey("ocean_giant");

    public static final RegistryKey<ConfiguredFeature<?, ?>> SURFACE_CRATER_MEDIUM_CF = cfKey("surface_crater_medium");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SURFACE_CRATER_LARGE_CF = cfKey("surface_crater_large");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SURFACE_CRATER_GIANT_CF = cfKey("surface_crater_giant");

    // 3. Объявляем Ключи для Размещения (Placed Features)
    public static final RegistryKey<PlacedFeature> UNDERGROUND_SMALL_PF = pfKey("underground_small");
    public static final RegistryKey<PlacedFeature> UNDERGROUND_MEDIUM_PF = pfKey("underground_medium");
    public static final RegistryKey<PlacedFeature> UNDERGROUND_LARGE_PF = pfKey("underground_large");
    public static final RegistryKey<PlacedFeature> UNDERGROUND_GIANT_PF = pfKey("underground_giant");

    public static final RegistryKey<PlacedFeature> OCEAN_SMALL_PF = pfKey("ocean_small");
    public static final RegistryKey<PlacedFeature> OCEAN_MEDIUM_PF = pfKey("ocean_medium");
    public static final RegistryKey<PlacedFeature> OCEAN_LARGE_PF = pfKey("ocean_large");
    public static final RegistryKey<PlacedFeature> OCEAN_GIANT_PF = pfKey("ocean_giant");

    public static final RegistryKey<PlacedFeature> SURFACE_CRATER_MEDIUM_PF = pfKey("surface_crater_medium");
    public static final RegistryKey<PlacedFeature> SURFACE_CRATER_LARGE_PF = pfKey("surface_crater_large");
    public static final RegistryKey<PlacedFeature> SURFACE_CRATER_GIANT_PF = pfKey("surface_crater_giant");

    public static void register() {
        Registry.register(Registries.FEATURE, Identifier.of(SkyStoneCelestialThreat.MOD_ID, "asteroid"), ASTEROID);
        Registry.register(Registries.FEATURE, Identifier.of(SkyStoneCelestialThreat.MOD_ID, "crater"), CRATER);
    }

    private static RegistryKey<ConfiguredFeature<?, ?>> cfKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(SkyStoneCelestialThreat.MOD_ID, name));
    }

    private static RegistryKey<PlacedFeature> pfKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(SkyStoneCelestialThreat.MOD_ID, name));
    }

    // Бутстрап для параметров объектов
    public static void bootstrapConfigured(Registerable<ConfiguredFeature<?, ?>> context) {
        context.register(UNDERGROUND_SMALL_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(1.3f, 0.0f, false)));
        context.register(UNDERGROUND_MEDIUM_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(2.2f, 1.0f, false)));
        context.register(UNDERGROUND_LARGE_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(3.2f, 1.5f, false)));
        context.register(UNDERGROUND_GIANT_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(4.2f, 2.2f, false)));

        context.register(OCEAN_SMALL_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(1.3f, 0.0f, true)));
        context.register(OCEAN_MEDIUM_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(2.2f, 1.0f, true)));
        context.register(OCEAN_LARGE_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(3.2f, 1.5f, true)));
        context.register(OCEAN_GIANT_CF, new ConfiguredFeature<>(ASTEROID, new AsteroidFeatureConfig(4.3f, 2.3f, true)));

        context.register(SURFACE_CRATER_MEDIUM_CF, new ConfiguredFeature<>(CRATER, new CraterFeatureConfig(9, 5, 1.3f, 0.0f)));
        context.register(SURFACE_CRATER_LARGE_CF, new ConfiguredFeature<>(CRATER, new CraterFeatureConfig(15, 8, 2.2f, 1.0f)));
        context.register(SURFACE_CRATER_GIANT_CF, new ConfiguredFeature<>(CRATER, new CraterFeatureConfig(24, 12, 2.2f, 1.0f)));
    }

    // Бутстрап для правил спавна (Используем RarityFilterPlacementModifier)
    public static void bootstrapPlaced(Registerable<PlacedFeature> context) {
        var configuredLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        context.register(UNDERGROUND_SMALL_PF, new PlacedFeature(configuredLookup.getOrThrow(UNDERGROUND_SMALL_CF), List.of(RarityFilterPlacementModifier.of(4))));
        context.register(UNDERGROUND_MEDIUM_PF, new PlacedFeature(configuredLookup.getOrThrow(UNDERGROUND_MEDIUM_CF), List.of(RarityFilterPlacementModifier.of(15))));
        context.register(UNDERGROUND_LARGE_PF, new PlacedFeature(configuredLookup.getOrThrow(UNDERGROUND_LARGE_CF), List.of(RarityFilterPlacementModifier.of(45))));
        context.register(UNDERGROUND_GIANT_PF, new PlacedFeature(configuredLookup.getOrThrow(UNDERGROUND_GIANT_CF), List.of(RarityFilterPlacementModifier.of(120))));

        context.register(OCEAN_SMALL_PF, new PlacedFeature(configuredLookup.getOrThrow(OCEAN_SMALL_CF), List.of(RarityFilterPlacementModifier.of(300))));
        context.register(OCEAN_MEDIUM_PF, new PlacedFeature(configuredLookup.getOrThrow(OCEAN_MEDIUM_CF), List.of(RarityFilterPlacementModifier.of(600))));
        context.register(OCEAN_LARGE_PF, new PlacedFeature(configuredLookup.getOrThrow(OCEAN_LARGE_CF), List.of(RarityFilterPlacementModifier.of(1200))));
        context.register(OCEAN_GIANT_PF, new PlacedFeature(configuredLookup.getOrThrow(OCEAN_GIANT_CF), List.of(RarityFilterPlacementModifier.of(4800))));

        context.register(SURFACE_CRATER_MEDIUM_PF, new PlacedFeature(configuredLookup.getOrThrow(SURFACE_CRATER_MEDIUM_CF), List.of(RarityFilterPlacementModifier.of(400))));
        context.register(SURFACE_CRATER_LARGE_PF, new PlacedFeature(configuredLookup.getOrThrow(SURFACE_CRATER_LARGE_CF), List.of(RarityFilterPlacementModifier.of(800))));
        context.register(SURFACE_CRATER_GIANT_PF, new PlacedFeature(configuredLookup.getOrThrow(SURFACE_CRATER_GIANT_CF), List.of(RarityFilterPlacementModifier.of(2000))));
    }
}