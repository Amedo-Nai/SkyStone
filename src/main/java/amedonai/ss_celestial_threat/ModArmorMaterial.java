package amedonai.ss_celestial_threat; // Укажи свой пакет материалов

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.List;

public class ModArmorMaterial {
    public static final RegistryEntry<ArmorMaterial> METEORITE_IRON = register("meteorite_iron", new ArmorMaterial(
            Util.make(new Object2IntOpenHashMap<>(), map -> {
                map.put(ArmorItem.Type.BOOTS, 3);
                map.put(ArmorItem.Type.LEGGINGS, 6);
                map.put(ArmorItem.Type.CHESTPLATE, 8);
                map.put(ArmorItem.Type.HELMET, 3);
                map.put(ArmorItem.Type.BODY, 11); // Для волка/лошади (при необходимости)
            }),
            15, // Зачаровываемость
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            () -> Ingredient.ofItems(ModItems.METEORITE_IRON_INGOT),
            List.of(new ArmorMaterial.Layer(Identifier.of(SkyStoneCelestialThreat.MOD_ID, "meteorite_iron"))), // Путь к текстуре модели брони
            2.0F, // Твёрдость (toughness)
            0F  // Сопротивление отбрасыванию (knockback resistance)
    ));

    private static RegistryEntry<ArmorMaterial> register(String id, ArmorMaterial material) {
        return Registry.registerReference(Registries.ARMOR_MATERIAL, Identifier.of(SkyStoneCelestialThreat.MOD_ID, id), material);
    }

    public static void registerAllArmorMaterials() {
        SkyStoneCelestialThreat.LOGGER.info("Registering Armor Materials for " + SkyStoneCelestialThreat.MOD_ID);
    }
}