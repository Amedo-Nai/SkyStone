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
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import vmua.skystone.ModItems;

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
		ModEntities.initialize(); // <-- ДОБАВЛЯЕМ СЮДА (в твоем фирменном стиле)

		// Поведение раздатчика: позволяет экипировать метеоритный щит на стойки, зомби и скелетов
		net.minecraft.block.DispenserBlock.registerBehavior(ModItems.METEORITE_IRON_SHIELD, net.minecraft.item.ArmorItem.DISPENSER_BEHAVIOR);

		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return ActionResult.PASS;
		});
		FabricModelPredicateProviderRegistry.register(ModItems.METEORITE_IRON_SHIELD, new Identifier("blocking"),
				(stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
		);

		LOGGER.info("SkyStone mod initialized!");
	}
}