package amedonai.ss_celestial_threat;

import amedonai.ss_celestial_threat.world.feature.ModFeatures;
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
import net.minecraft.registry.tag.BiomeTags;

public class SkyStoneCelestialThreat implements ModInitializer {
	public static final String MOD_ID = "skystone-celestial-threat";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final RegistryKey<ItemGroup> SKYSTONE_CELESTIAL_THREAT_GROUP_KEY = RegistryKey.of(
			Registries.ITEM_GROUP.getKey(),
			Identifier.of(MOD_ID, "skystone-celestial-threat_group")
	);

	public static final ItemGroup SKYSTONE_GROUP = Registry.register(
			Registries.ITEM_GROUP,
			SKYSTONE_CELESTIAL_THREAT_GROUP_KEY,
			FabricItemGroup.builder()
					.icon(() -> new ItemStack(ModBlocks.METEORITE_IRON_ORE))
					.displayName(Text.translatable("itemGroup." + MOD_ID + ".skystone-celestial-threat_group"))
					.entries((displayContext, entries) -> {

						// --- БЛОКИ (ModBlocks) ---
						entries.add(ModBlocks.METEORITE_IRON_ORE);
						entries.add(ModBlocks.METEORITE_IRON_BLOCK);
						entries.add(ModBlocks.METEORITE_IRON_DOOR);
						entries.add(ModBlocks.METEORITE_IRON_TRAPDOOR);
						entries.add(ModBlocks.METEORITE_IRON_PRESSURE_PLATE);

						entries.add(ModBlocks.GOLDEN_ANVIL);
						entries.add(ModBlocks.CHIPPED_GOLDEN_ANVIL);
						entries.add(ModBlocks.DAMAGED_GOLDEN_ANVIL);

						entries.add(ModBlocks.METEORITE_IRON_ANVIL);
						entries.add(ModBlocks.CHIPPED_METEORITE_IRON_ANVIL);
						entries.add(ModBlocks.DAMAGED_METEORITE_IRON_ANVIL);

						entries.add(ModBlocks.GOLDEN_DOOR);
						entries.add(ModBlocks.GOLDEN_TRAPDOOR);
						entries.add(ModBlocks.METEORITE_IRON_BARS);
						entries.add(ModBlocks.GOLDEN_BARS);

						entries.add(ModBlocks.SKY_STONE);
						entries.add(ModBlocks.SKY_COBBLESTONE);
						entries.add(ModBlocks.SKY_STONE_BRICKS);
						entries.add(ModBlocks.CRACKED_SKY_STONE_BRICKS);
						entries.add(ModBlocks.CHISELED_SKY_STONE_BRICKS);
						entries.add(ModBlocks.SMOOTH_SKY_STONE);

						entries.add(ModBlocks.SKY_STONE_SLAB);
						entries.add(ModBlocks.SKY_STONE_STAIRS);
						entries.add(ModBlocks.SKY_STONE_WALL);

						entries.add(ModBlocks.SMOOTH_SKY_STONE_SLAB);
						entries.add(ModBlocks.SMOOTH_SKY_STONE_STAIRS);
						entries.add(ModBlocks.SMOOTH_SKY_STONE_WALL);

						entries.add(ModBlocks.SKY_COBBLESTONE_SLAB);
						entries.add(ModBlocks.SKY_COBBLESTONE_STAIRS);
						entries.add(ModBlocks.SKY_COBBLESTONE_WALL);

						entries.add(ModBlocks.SKY_STONE_BRICKS_SLAB);
						entries.add(ModBlocks.SKY_STONE_BRICKS_STAIRS);
						entries.add(ModBlocks.SKY_STONE_BRICKS_WALL);

						entries.add(ModBlocks.SKY_STONE_FURNACE);
						entries.add(ModBlocks.SKY_STONE_BUTTON);

						// --- ПРЕДМЕТЫ И ИНСТРУМЕНТЫ (ModItems) ---
						entries.add(ModItems.SKY_STONE_SWORD);
						entries.add(ModItems.SKY_STONE_PICKAXE);
						entries.add(ModItems.SKY_STONE_AXE);
						entries.add(ModItems.SKY_STONE_SHOVEL);
						entries.add(ModItems.SKY_STONE_HOE);

						entries.add(ModItems.METEORITE_IRON_INGOT);
						entries.add(ModItems.METEORITE_IRON_NUGGET);
						entries.add(ModItems.METEORITE_IRON_SWORD);
						entries.add(ModItems.METEORITE_IRON_PICKAXE);
						entries.add(ModItems.METEORITE_IRON_AXE);
						entries.add(ModItems.METEORITE_IRON_SHOVEL);
						entries.add(ModItems.METEORITE_IRON_HOE);
						entries.add(ModItems.METEORITE_IRON_SHIELD);

						entries.add(ModItems.METEORITE_IRON_HELMET);
						entries.add(ModItems.METEORITE_IRON_CHESTPLATE);
						entries.add(ModItems.METEORITE_IRON_LEGGINGS);
						entries.add(ModItems.METEORITE_IRON_BOOTS);

						entries.add(ModItems.METEORITE_IRON_GOLEM_SPAWN_EGG);

						entries.add(ModItems.GOLDEN_BUCKET);
						entries.add(ModItems.GOLDEN_WATER_BUCKET);
						entries.add(ModItems.GOLDEN_LAVA_BUCKET);
						entries.add(ModItems.GOLDEN_MILK_BUCKET);
						entries.add(ModItems.GOLDEN_COD_BUCKET);
						entries.add(ModItems.GOLDEN_SALMON_BUCKET);
						entries.add(ModItems.GOLDEN_PUFFERFISH_BUCKET);
						entries.add(ModItems.GOLDEN_TROPICAL_FISH_BUCKET);

						entries.add(ModItems.METEORITE_IRON_BUCKET);
						entries.add(ModItems.METEORITE_IRON_WATER_BUCKET);
						entries.add(ModItems.METEORITE_IRON_LAVA_BUCKET);
						entries.add(ModItems.METEORITE_IRON_MILK_BUCKET);
						entries.add(ModItems.METEORITE_IRON_COD_BUCKET);
						entries.add(ModItems.METEORITE_IRON_SALMON_BUCKET);
						entries.add(ModItems.METEORITE_IRON_PUFFERFISH_BUCKET);
						entries.add(ModItems.METEORITE_IRON_TROPICAL_FISH_BUCKET);
					})
					.build()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading base content for SkyStone: Celestial Threat...");

		// Включаем регистрацию базовых фич
		ModFeatures.register();

		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModArmorMaterial.registerAllArmorMaterials();
		ModItems.initialize();
		ModScreenHandlers.registerAllScreenHandlers();

		// 1. ПОДЗЕМНЫЕ МЕТЕОРИТЫ
		String[] undergroundFeatures = {"underground_small", "underground_medium", "underground_large", "underground_giant"};
		for (String feature : undergroundFeatures) {
			BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(),
					GenerationStep.Feature.UNDERGROUND_ORES,
					RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, feature))
			);
		}

		// 2. ОКЕАНИЧЕСКИЕ МЕТЕОРИТЫ
		String[] oceanFeatures = {"ocean_small", "ocean_medium", "ocean_large", "ocean_giant"};
		for (String feature : oceanFeatures) {
			BiomeModifications.addFeature(
					BiomeSelectors.tag(net.minecraft.registry.tag.BiomeTags.IS_OCEAN), // ИСПРАВЛЕНО: теперь путь строго net.minecraft.registry.tag
					GenerationStep.Feature.UNDERGROUND_ORES,
					RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, feature))
			);
		}

		// 3. КРАТЕРЫ
		String[] craterFeatures = {"surface_crater_medium", "surface_crater_large", "surface_crater_giant"};
		for (String feature : craterFeatures) {
			BiomeModifications.addFeature(
					BiomeSelectors.foundInOverworld(),
					GenerationStep.Feature.SURFACE_STRUCTURES,
					RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(MOD_ID, feature))
			);
		}

		DispenserBlock.registerBehavior(ModItems.METEORITE_IRON_SHIELD, ArmorItem.DISPENSER_BEHAVIOR);

		FuelRegistry.INSTANCE.add(ModItems.METEORITE_IRON_LAVA_BUCKET, 20000);
		FuelRegistry.INSTANCE.add(ModItems.GOLDEN_LAVA_BUCKET, 20000);
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}