package amedonai.ss_celestial_threat.client;

import amedonai.ss_celestial_threat.ModBlocks;
import amedonai.ss_celestial_threat.ModItems;
import amedonai.ss_celestial_threat.ModScreenHandlers;
import amedonai.ss_celestial_threat.SkyStoneCelestialThreat;
import amedonai.ss_celestial_threat.client.screen.SkyStoneFurnaceScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class SkyStoneCelestialThreatClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// 1. Рендеринг прозрачных блоков (Остался прежним через Fabric API)
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_TRAPDOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_TRAPDOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_BARS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_BARS, RenderLayer.getCutout());

		// 2. Предикат блокирования для кастомного щита (Ванильный реестр 1.21.1)
		ModelPredicateProviderRegistry.register(
				ModItems.METEORITE_IRON_SHIELD,
				Identifier.of("minecraft", "blocking"),
				(stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
		);

		// 3. Регистрация рендерера моба (Новый синтаксис Fabric API)
		// Если твой класс рендерера еще не обновлен, возможно, внутри него тоже придется поправить конструктор
		// EntityRendererRegistry.INSTANCE переименован в статический вызов метода
		// Внимание: раскомментируй, когда ModEntities и твой рендерер будут готовы!
        /*
        EntityRendererRegistry.register(ModEntities.METEORITE_IRON_GOLEM,
                (context) -> new MeteoriteIronGolemEntityRenderer(context)
        );
        */

		// 4. Регистрация экрана печи (ScreenRegistry заменен на ванильный HandledScreens)
		HandledScreens.register(ModScreenHandlers.SKY_STONE_FURNACE, SkyStoneFurnaceScreen::new);

		// 5. Регистрация фабрики частиц (Фиолетовое пламя)
		// Внимание: раскомментируй, когда ModParticles будет готов!
        /*
        ParticleFactoryRegistry.getInstance().register(ModParticles.SKY_STONE_FLAME, FlameParticle.Factory::new);
        */
	}
}