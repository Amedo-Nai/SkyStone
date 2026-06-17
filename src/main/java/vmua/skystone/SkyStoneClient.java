package vmua.skystone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry; // Правильный импорт от Fabric
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.util.Identifier;

public class SkyStoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Рендеринг прозрачных блоков
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_TRAPDOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.METEORITE_IRON_BARS, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GOLDEN_BARS, RenderLayer.getCutout());

        // Твоя регистрация спрайтов для атласа
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE).register((textureId, registry) -> {
            registry.register(new Identifier("skystone", "entity/meteorite_iron_shield_base"));
            registry.register(new Identifier("skystone", "entity/meteorite_iron_shield_base_no_pattern"));
        });
        FabricModelPredicateProviderRegistry.register(ModItems.METEORITE_IRON_SHIELD, new Identifier("blocking"),
                (stack, world, entity) -> entity != null && entity.isUsingItem() && entity.getActiveItem() == stack ? 1.0F : 0.0F
        );
    }
}