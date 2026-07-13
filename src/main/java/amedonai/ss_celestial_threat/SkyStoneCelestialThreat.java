package amedonai.ss_celestial_threat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyStoneCelestialThreat implements ModInitializer {
	public static final String MOD_ID = "skystone-celestial-threat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// 1. Создаем ключ для нашей творческой вкладки
	public static final RegistryKey<ItemGroup> SKYSTONE_GROUP_KEY = RegistryKey.of(
			Registries.ITEM_GROUP.getKey(),
			Identifier.of(MOD_ID, "skystone_group")
	);

	// 2. Регистрируем вкладку по стандартам 1.21.1
	public static final ItemGroup SKYSTONE_GROUP = Registry.register(
			Registries.ITEM_GROUP,
			SKYSTONE_GROUP_KEY,
			FabricItemGroup.builder()
					.icon(() -> new ItemStack(ModBlocks.METEORITE_IRON_ORE)) // Иконка вкладки
					.displayName(Text.translatable("itemGroup." + MOD_ID + ".skystone_group")) // Локализация имени
					.entries((displayContext, entries) -> {
						// Сюда перечисляешь ВСЕ свои предметы и блоки, чтобы они появились в креативе:
						entries.add(ModBlocks.METEORITE_IRON_ORE);
						entries.add(ModItems.METEORITE_IRON_INGOT);
						entries.add(ModItems.METEORITE_IRON_HELMET);
						entries.add(ModItems.METEORITE_IRON_CHESTPLATE);
						entries.add(ModItems.METEORITE_IRON_LEGGINGS);
						entries.add(ModItems.METEORITE_IRON_BOOTS);
						// И так далее...
					})
					.build()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading base content for SkyStone: Celestial Threat...");

		ModBlocks.initialize();
		ModArmorMaterial.registerAllArmorMaterials();
		ModItems.initialize();
		ModScreenHandlers.registerAllScreenHandlers();

		// 3. ДОБАВЛЯЕМ МЕТЕОРИТЫ В МИР (Синтаксис обновлен под 1.21.1)
		// 1. ПОДЗЕМНЫЕ МЕТЕОРИТЫ
		String[] undergroundFeatures = {"underground_small", "underground_medium", "underground_large", "underground_giant"};
		for (String feature : undergroundFeatures) {
			BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(),
					GenerationStep.Feature.UNDERGROUND_ORES,
					RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, feature)) // ТУТ Placed
			);
		}

// 2. ОКЕАНИЧЕСКИЕ МЕТЕОРИТЫ
		String[] oceanFeatures = {"ocean_small", "ocean_medium", "ocean_large", "ocean_giant"};
		for (String feature : oceanFeatures) {
			BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(),
					GenerationStep.Feature.UNDERGROUND_ORES,
					RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, feature)) // И ТУТ Placed
			);
		}

// 3. КРАТЕРЫ
		String[] craterFeatures = {"surface_crater_medium", "surface_crater_large", "surface_crater_giant"};
		for (String feature : craterFeatures) {
			BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(),
					GenerationStep.Feature.SURFACE_STRUCTURES,
					RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, feature)) // И ТУТ Placed
			);
		}

		// Поведение раздатчика (Dispenser)
		DispenserBlock.registerBehavior(ModItems.METEORITE_IRON_SHIELD, ArmorItem.DISPENSER_BEHAVIOR);

		// Регистрация топлива в печь
		FuelRegistry.INSTANCE.add(ModItems.METEORITE_IRON_LAVA_BUCKET, 20000);
		FuelRegistry.INSTANCE.add(ModItems.GOLDEN_LAVA_BUCKET, 20000);
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}