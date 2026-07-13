package amedonai.ss_celestial_threat.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public class CraterFeatureConfig implements FeatureConfig {
    public static final Codec<CraterFeatureConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                    Codec.INT.fieldOf("crater_radius").forGetter((config) -> config.craterRadius),
                    Codec.INT.fieldOf("crater_depth").forGetter((config) -> config.craterDepth),
                    Codec.FLOAT.fieldOf("asteroid_outer_radius").forGetter((config) -> config.asteroidOuterRadius),
                    Codec.FLOAT.fieldOf("asteroid_inner_radius").forGetter((config) -> config.asteroidInnerRadius)
            ).apply(instance, CraterFeatureConfig::new)
    );

    public final int craterRadius;
    public final int craterDepth;
    public final float asteroidOuterRadius;
    public final float asteroidInnerRadius;

    public CraterFeatureConfig(int craterRadius, int craterDepth, float asteroidOuterRadius, float asteroidInnerRadius) {
        this.craterRadius = craterRadius;
        this.craterDepth = craterDepth;
        this.asteroidOuterRadius = asteroidOuterRadius;
        this.asteroidInnerRadius = asteroidInnerRadius;
    }
}