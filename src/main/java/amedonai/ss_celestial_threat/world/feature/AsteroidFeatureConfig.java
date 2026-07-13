package amedonai.ss_celestial_threat.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public class AsteroidFeatureConfig implements FeatureConfig {

    // Кодек для сериализации данных фичи в JSON (в 1.21.1 синтаксис полностью совпадает)
    public static final Codec<AsteroidFeatureConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.FLOAT.fieldOf("outer_radius").forGetter((config) -> config.outerRadius),
                    Codec.FLOAT.fieldOf("inner_radius").forGetter((config) -> config.innerRadius),
                    Codec.BOOL.fieldOf("is_ocean_floor").forGetter((config) -> config.isOceanFloor)
            ).apply(instance, AsteroidFeatureConfig::new)
    );

    public final float outerRadius;
    public final float innerRadius;
    public final boolean isOceanFloor;

    public AsteroidFeatureConfig(float outerRadius, float innerRadius, boolean isOceanFloor) {
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.isOceanFloor = isOceanFloor;
    }
}