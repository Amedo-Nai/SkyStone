package amedonai.ss_celestial_threat.world.feature;

import amedonai.ss_celestial_threat.SkyStoneCelestialThreat;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {
    public static final Feature<AsteroidFeatureConfig> ASTEROID = new AsteroidFeature(AsteroidFeatureConfig.CODEC);
    public static final Feature<CraterFeatureConfig> CRATER = new CraterFeature(CraterFeatureConfig.CODEC);

    public static void register() {
        Registry.register(Registries.FEATURE, Identifier.of(SkyStoneCelestialThreat.MOD_ID, "asteroid"), ASTEROID);
        Registry.register(Registries.FEATURE, Identifier.of(SkyStoneCelestialThreat.MOD_ID, "crater"), CRATER);
    }
}