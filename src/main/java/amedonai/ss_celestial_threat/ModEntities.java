package amedonai.ss_celestial_threat;

import amedonai.ss_celestial_threat.entity.MeteoriteIronGolemEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final RegistryKey<EntityType<?>> METEORITE_IRON_GOLEM_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(SkyStoneCelestialThreat.MOD_ID, "meteorite_iron_golem"));

    //Регистрируем сам тип сущности в реестре Майнкрафта
    public static final EntityType<MeteoriteIronGolemEntity> METEORITE_IRON_GOLEM = Registry.register(
            Registries.ENTITY_TYPE,
            METEORITE_IRON_GOLEM_KEY,
            EntityType.Builder.create(MeteoriteIronGolemEntity::new, SpawnGroup.MISC)
                    .dimensions(1.4F, 2.7F) // Размеры моба
                    .build("meteorite_iron_golem") // ИСПРАВЛЕНО: передаем чистую строку вместо ключа
    );

    public static void initialize() {
        // Регистрируем кастомные атрибуты
        FabricDefaultAttributeRegistry.register(METEORITE_IRON_GOLEM, MeteoriteIronGolemEntity.createMeteoriteGolemAttributes());
    }
}