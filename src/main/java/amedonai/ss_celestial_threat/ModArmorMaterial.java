package amedonai.ss_celestial_threat;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;

public class ModArmorMaterial {

    // 1. Меняем тип на ArmorMaterial и метод на Registry.register
    public static final ArmorMaterial METEORITE_IRON = Registry.register(
            Registries.ARMOR_MATERIAL,
            Identifier.of(SkyStoneCelestialThreat.MOD_ID, "meteorite_iron"),
            new ArmorMaterial(
                    Map.of(
                            ArmorItem.Type.BOOTS, 3,
                            ArmorItem.Type.LEGGINGS, 5,
                            ArmorItem.Type.CHESTPLATE, 7,
                            ArmorItem.Type.HELMET, 3
                    ),
                    20, // Зачаровываемость
                    SoundEvents.ITEM_ARMOR_EQUIP_IRON, // Звук экипировки
                    () -> Ingredient.ofItems(ModItems.METEORITE_IRON_INGOT), // Ингредиент для починки
                    List.of(new ArmorMaterial.Layer(Identifier.of(SkyStoneCelestialThreat.MOD_ID, "meteorite_iron"))),
                    1.0F, // Стойкость брони
                    0.0F  // Сопротивление отбрасыванию
            )
    );

    // Пустой метод-помощник, чтобы заставить Java вовремя загрузить этот класс
    public static void registerAllArmorMaterials() {
    }
}