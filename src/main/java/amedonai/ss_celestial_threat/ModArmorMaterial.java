package amedonai.ss_celestial_threat;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class ModArmorMaterial {

    // Регистрируем наш материал в ванильном реестре брони
    public static final RegistryEntry<ArmorMaterial> METEORITE_IRON = Registry.registerReference(
            Registries.ARMOR_MATERIAL,
            Identifier.of(SkyStoneCelestialThreat.MOD_ID, "meteorite_iron"),
            new ArmorMaterial(
                    // 1. Карта защиты для каждого элемента брони (твои старые значения)
                    Map.of(
                            ArmorItem.Type.BOOTS, 3,
                            ArmorItem.Type.LEGGINGS, 5,
                            ArmorItem.Type.CHESTPLATE, 7,
                            ArmorItem.Type.HELMET, 3
                    ),
                    20, // Зачаровываемость (enchantability)
                    SoundEvents.ITEM_ARMOR_EQUIP_IRON, // Звук экипировки
                    () -> Ingredient.ofItems(ModItems.METEORITE_IRON_INGOT), // Ингредиент для починки
                    // 2. Слои текстур брони (в 1.21 текстуры привязаны прямо к материалу)
                    List.of(new ArmorMaterial.Layer(Identifier.of(SkyStoneCelestialThreat.MOD_ID, "meteorite_iron"))),
                    1.0F, // Стойкость брони (toughness)
                    0.0F  // Сопротивление отбрасыванию (knockback resistance)
            )
    );
}