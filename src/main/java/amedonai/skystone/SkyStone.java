package amedonai.skystone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.block.entity.BlockEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.EquipmentSlot;
import java.util.UUID;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import amedonai.skystone.world.feature.ModFeatures;

public class SkyStone implements ModInitializer {
	public static final String MOD_ID = "skystone";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final ItemGroup SKYSTONE_GROUP = FabricItemGroupBuilder.build(
			new Identifier(MOD_ID, "skystone_group"),
			() -> new ItemStack(ModBlocks.METEORITE_IRON_ORE)
	);

	// Оставляем только одно чистое глобальное хранилище для падения
	private static final java.util.Map<UUID, Double> fallStartHeights = new java.util.HashMap<>();

	// Оптимизированный метод проверки брони — БЕЗ спамящих логов в консоль
	private static boolean hasFullMeteoriteArmor(ServerPlayerEntity player) {
		return player.getEquippedStack(EquipmentSlot.HEAD).getItem() == ModItems.METEORITE_IRON_HELMET
				&& player.getEquippedStack(EquipmentSlot.CHEST).getItem() == ModItems.METEORITE_IRON_CHESTPLATE
				&& player.getEquippedStack(EquipmentSlot.LEGS).getItem() == ModItems.METEORITE_IRON_LEGGINGS
				&& player.getEquippedStack(EquipmentSlot.FEET).getItem() == ModItems.METEORITE_IRON_BOOTS;
	}

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModBlockEntities.registerAllBlockEntities();
		ModItems.initialize();
		ModEntities.initialize();
		ModFeatures.register();
		ModScreenHandlers.registerAllScreenHandlers();
		ModParticles.registerParticles();

		// Генерируем метеориты
		String[] undergroundFeatures = {"underground_small", "underground_medium", "underground_large", "underground_giant"};
		for (String feature : undergroundFeatures) {
			net.fabricmc.fabric.api.biome.v1.BiomeModifications.addFeature(
					net.fabricmc.fabric.api.biome.v1.BiomeSelectors.foundInOverworld(),
					net.minecraft.world.gen.GenerationStep.Feature.UNDERGROUND_ORES,
					net.minecraft.util.registry.RegistryKey.of(net.minecraft.util.registry.Registry.CONFIGURED_FEATURE_WORLDGEN, new net.minecraft.util.Identifier(MOD_ID, feature))
			);
		}

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
					net.minecraft.world.gen.GenerationStep.Feature.SURFACE_STRUCTURES,
					net.minecraft.util.registry.RegistryKey.of(net.minecraft.util.registry.Registry.CONFIGURED_FEATURE_WORLDGEN, new net.minecraft.util.Identifier(MOD_ID, feature))
			);
		}

		net.minecraft.block.DispenserBlock.registerBehavior(ModItems.METEORITE_IRON_SHIELD, net.minecraft.item.ArmorItem.DISPENSER_BEHAVIOR);

		net.fabricmc.fabric.api.event.player.UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return net.minecraft.util.ActionResult.PASS;
		});

		// ИСПРАВЛЕНО: Теперь выдает "meteorite_obsidian" (в соответствии с массивом и файлом JSON)
		PlayerBlockBreakEvents.AFTER.register((World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) -> {
			if (!world.isClient && player instanceof ServerPlayerEntity) {
				ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

				if (state.isOf(Blocks.OBSIDIAN) && serverPlayer.getMainHandStack().getItem() == ModItems.METEORITE_IRON_PICKAXE) {
					AdvancementHelper.grantAdvancement(serverPlayer, "meteorite_obsidian");
				}
			}
		});

		// Перечень всех необходимых достижений для проверки финальной книги
		final String[] ALL_REQUIRED_ADVANCEMENTS = {
				"skystone:sky_stone_root", //Привет из космоса!+
				"skystone:sky_stone_furnace", //Небесный очаг+
				"skystone:sky_stone_tools", //Внеземной сетап+
				"skystone:golden_bucket_with_a_fish", //Роскошная рыбалка+
				"skystone:golden_anvil", //Золотой мастер+
				"skystone:meteorite_iron_ingot", //Небесная руда+
				"skystone:meteorite_iron_armor", //Закаленный небом+
				"skystone:meteorite_gravity", //Вера в гравитацию+
				"skystone:meteorite_obsidian", //Сокрушая земное+
				"skystone:meteorite_iron_tools", //Осколки сверхновой+
				"skystone:meteorite_shield", //Отражение угрозы+
				"skystone:meteorite_iron_anvil", //Тяжелый металл
				"skystone:meteorite_iron_bucket_with_a_fish", //Космический улов
				"skystone:meteorite_iron_golem", //Небесный монолит+
				"skystone:golem_wither" //Атака титанов
		};

		final int[] tickCounter = {0};

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				UUID uuid = player.getUuid();
				boolean hasFullSet = hasFullMeteoriteArmor(player);

				if (player.isOnGround()) {
					if (fallStartHeights.containsKey(uuid)) {
						double startY = fallStartHeights.remove(uuid);
						double distanceFallen = startY - player.getY();
						boolean isWet = player.isTouchingWater() || player.isSubmergedInWater();

						System.out.println(String.format("[SkyStone Debug] Landed! Fallen: %.2f blocks | Wet: %b | Alive: %b",
								distanceFallen, isWet, player.isAlive()));

						if (player.isAlive() && distanceFallen >= 100.0 && hasFullSet && !isWet) {
							AdvancementHelper.grantAdvancement(player, "meteorite_gravity");
						}
					}
				} else {
					if (hasFullSet && player.getVelocity().y < -0.1) {
						if (!fallStartHeights.containsKey(uuid)) {
							fallStartHeights.put(uuid, player.getY());
						}
					} else if (!hasFullSet) {
						fallStartHeights.remove(uuid);
					}
				}
			}

			tickCounter[0]++;
			if (tickCounter[0] >= 20) {
				tickCounter[0] = 0;

				for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
					Identifier gratitudeId = new Identifier(MOD_ID, "creators_gratitude");
					Advancement gratitudeAdv = server.getAdvancementLoader().get(gratitudeId);

					if (gratitudeAdv != null && player.getAdvancementTracker().getProgress(gratitudeAdv).isDone()) {
						continue;
					}

					boolean allCompleted = true;
					for (String advPath : ALL_REQUIRED_ADVANCEMENTS) {
						Identifier id = new Identifier(advPath);
						Advancement adv = server.getAdvancementLoader().get(id);
						if (adv == null || !player.getAdvancementTracker().getProgress(adv).isDone()) {
							allCompleted = false;
							break;
						}
					}

					if (allCompleted) {
						AdvancementHelper.grantAdvancement(player, "creators_gratitude");

						ItemStack book = new ItemStack(Items.WRITTEN_BOOK);
						NbtCompound tag = book.getOrCreateTag();

						tag.putString("title", "SkyStone Creator's Book");
						tag.putString("author", "AmedoNai");

						NbtList pages = new NbtList();
						pages.add(NbtString.of("{\"translate\":\"book.skystone.creators_gratitude.page1\"}"));
						pages.add(NbtString.of("{\"translate\":\"book.skystone.creators_gratitude.page2\"}"));
						pages.add(NbtString.of("{\"translate\":\"book.skystone.creators_gratitude.page3\"}"));
						tag.put("pages", pages);

						book.setCustomName(new net.minecraft.text.TranslatableText("book.skystone.creators_gratitude.title"));

						if (!player.inventory.insertStack(book)) {
							player.dropItem(book, false);
						}
					}
				}
			}
		});

		net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry.register(ModItems.METEORITE_IRON_SHIELD, new net.minecraft.util.Identifier("blocking"),
				(stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
		);
		net.fabricmc.fabric.api.registry.FuelRegistry.INSTANCE.add(ModItems.METEORITE_IRON_LAVA_BUCKET, 20000);
		net.fabricmc.fabric.api.registry.FuelRegistry.INSTANCE.add(ModItems.GOLDEN_LAVA_BUCKET, 20000);

		LOGGER.info("SkyStone mod initialized!");
	}
}