package amedonai.ss_celestial_threat.client;

import amedonai.ss_celestial_threat.*;
import amedonai.ss_celestial_threat.client.render.MeteoriteIronGolemEntityRenderer;
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
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_TRAPDOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_TRAPDOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_BARS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_BARS, RenderLayer.getCutout());

		ModelPredicateProviderRegistry.register(
				ModItems.METEORITE_IRON_SHIELD,
				Identifier.of("minecraft", "blocking"),
				(stack, world, entity, seed) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
		);

        /*
        EntityRendererRegistry.register(ModEntities.METEORITE_IRON_GOLEM,
                (context) -> new MeteoriteIronGolemEntityRenderer(context)
        );
        */

		HandledScreens.register(ModScreenHandlers.SKY_STONE_FURNACE, SkyStoneFurnaceScreen::new);
		EntityRendererRegistry.register(ModEntities.METEORITE_IRON_GOLEM, MeteoriteIronGolemEntityRenderer::new);
		ParticleFactoryRegistry.getInstance().register(ModParticles.SKY_STONE_FLAME, FlameParticle.Factory::new);
	}
}