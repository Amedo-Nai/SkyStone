package amedonai.skystone;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import amedonai.skystone.entity.MeteoriteIronGolemEntity;

public class ModEntities {

    // 1. Регистрируем сам тип сущности в реестре Майнкрафта
    public static final EntityType<MeteoriteIronGolemEntity> METEORITE_IRON_GOLEM = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier(SkyStone.MOD_ID, "meteorite_iron_golem"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, MeteoriteIronGolemEntity::new)
                    .dimensions(EntityDimensions.changing(1.4F, 2.7F))
                    .build()
    );
    public static void initialize() {
        FabricDefaultAttributeRegistry.register(METEORITE_IRON_GOLEM, MeteoriteIronGolemEntity.createMeteoriteGolemAttributes());
    }
}