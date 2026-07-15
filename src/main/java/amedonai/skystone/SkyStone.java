package amedonai.skystone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import amedonai.skystone.world.feature.ModFeatures;

public class SkyStone implements ModInitializer {
	public static final String MOD_ID = "skystone";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	// Создаем нашу вкладку. В качестве иконки берем метеоритную руду
	public static final ItemGroup SKYSTONE_GROUP = FabricItemGroupBuilder.build(
			new Identifier(MOD_ID, "skystone_group"),
			() -> new ItemStack(ModBlocks.METEORITE_IRON_ORE)
	);

	@Override
	public void onInitialize() {
		// Инициализация базовых элементов мода
		ModBlocks.initialize();
		ModBlockEntities.registerAllBlockEntities();
		ModItems.initialize();
		ModEntities.initialize();
		ModFeatures.register();
		ModScreenHandlers.registerAllScreenHandlers();
		ModParticles.registerParticles();

		// ДОБАВЛЯЕМ ПОДЗЕМНЫЕ МЕТЕОРИТЫ
		String[] undergroundFeatures = {"underground_small", "underground_medium", "underground_large", "underground_giant"};
		for (String feature : undergroundFeatures) {
			net.fabricmc.fabric.api.biome.v1.BiomeModifications.addFeature(
					net.fabricmc.fabric.api.biome.v1.BiomeSelectors.foundInOverworld(),
					net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_ORES,
					net.minecraft.util.registry.RegistryKey.of(net.minecraft.util.registry.Registry.CONFIGURED_FEATURE_WORLDGEN, new net.minecraft.util.Identifier(MOD_ID, feature))
			);
		}

		// ДОБАВЛЯЕМ ОКЕАНИЧЕСКИЕ МЕТЕОРИТЫ НА ДНО
		String[] oceanFeatures = {"ocean_small", "ocean_medium", "ocean_large", "ocean_giant"};
		for (String feature : oceanFeatures) {
			net.fabricmc.fabric.api.biome.v1.BiomeModifications.addFeature(
					net.fabricmc.fabric.api.biome.v1.BiomeSelectors.foundInOverworld(),
					net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_ORES,
					net.minecraft.util.registry.RegistryKey.of(net.minecraft.util.registry.Registry.CONFIGURED_FEATURE_WORLDGEN, new net.minecraft.util.Identifier(MOD_ID, feature))
			);
		}

		String[] craterFeatures = {"surface_crater_medium", "surface_crater_large", "surface_crater_giant"};
		for (String feature : craterFeatures) {
			net.fabricmc.fabric.api.biome.v1.BiomeModifications.addFeature(
					net.fabricmc.fabric.api.biome.v1.BiomeSelectors.foundInOverworld(),
					net.minecraft.world.gen.GenerationStep.Feature.SURFACE_STRUCTURES, // Важно: этап поверхностных структур!
					net.minecraft.util.registry.RegistryKey.of(net.minecraft.util.registry.Registry.CONFIGURED_FEATURE_WORLDGEN, new net.minecraft.util.Identifier(MOD_ID, feature))
			);
		}

		// Поведение раздатчика: позволяет экипировать метеоритный щит на стойки, зомби и скелетов
		net.minecraft.block.DispenserBlock.registerBehavior(ModItems.METEORITE_IRON_SHIELD, net.minecraft.item.ArmorItem.DISPENSER_BEHAVIOR);

		net.fabricmc.fabric.api.event.player.UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return net.minecraft.util.ActionResult.PASS;
		});

		// Предикат блокирования для щита (чтобы прожималась анимация защиты)
		net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry.register(ModItems.METEORITE_IRON_SHIELD, new net.minecraft.util.Identifier("blocking"),
				(stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
		);
		net.fabricmc.fabric.api.registry.FuelRegistry.INSTANCE.add(ModItems.METEORITE_IRON_LAVA_BUCKET, 20000);
		net.fabricmc.fabric.api.registry.FuelRegistry.INSTANCE.add(ModItems.GOLDEN_LAVA_BUCKET, 20000);

		LOGGER.info("SkyStone mod initialized!");
	}
}