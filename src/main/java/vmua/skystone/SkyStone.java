package vmua.skystone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkyStone implements ModInitializer {
	public static final String MOD_ID = "skystone";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	// Создаем нашу вкладку. В качестве иконки берем метеоритную руду
	public static final ItemGroup SKYSTONE_GROUP = FabricItemGroupBuilder.build(
			new Identifier(MOD_ID, "skystone_group"),
			() -> new ItemStack(ModBlocks.METEORITE_ORE)
	);

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModItems.initialize();

		// Исправленная логика доения коров для 1.16.5 Yarn (без .isOf)
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			if (entity instanceof CowEntity && !((CowEntity) entity).isBaby()) {
				ItemStack stack = player.getStackInHand(hand);

				// Проверяем тип предмета через .getItem() ==
				if (stack.getItem() == ModItems.GOLDEN_BUCKET || stack.getItem() == ModItems.METEORITE_IRON_BUCKET) {
					player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);

					// Здесь тоже используем классическое сравнение
					ItemStack milkStack = new ItemStack(stack.getItem() == ModItems.GOLDEN_BUCKET ? ModItems.GOLDEN_MILK_BUCKET : ModItems.METEORITE_IRON_MILK_BUCKET);

					if (!player.abilities.creativeMode) {
						stack.decrement(1);
						if (stack.isEmpty()) {
							player.setStackInHand(hand, milkStack);
						} else if (!player.inventory.insertStack(milkStack)) {
							player.dropItem(milkStack, false);
						}
					}
					return ActionResult.success(world.isClient());
				}
			}
			return ActionResult.PASS;
		});

		LOGGER.info("SkyStone mod initialized!");
	}
}