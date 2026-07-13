package amedonai.ss_celestial_threat.client.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode; // ИСПРАВЛЕННЫЙ ИМПОРТ
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import amedonai.ss_celestial_threat.ModItems;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    @Shadow @Final
    private ShieldEntityModel modelShield;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderMeteoriteIronShield(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (stack.getItem() == ModItems.METEORITE_IRON_SHIELD) {
            BannerPatternsComponent patterns = stack.get(DataComponentTypes.BANNER_PATTERNS);
            boolean hasBanner = patterns != null && !patterns.layers().isEmpty();

            matrices.push();
            matrices.scale(1.0F, -1.0F, -1.0F);

            if (!hasBanner) {
                Identifier rawTextureId = Identifier.of("skystone", "textures/entity/meteorite_iron_shield_base.png");

                VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(
                        vertexConsumers,
                        RenderLayer.getEntitySolid(rawTextureId),
                        true,
                        stack.hasGlint()
                );

                this.modelShield.getHandle().render(matrices, vertexConsumer, light, overlay);
                this.modelShield.getPlate().render(matrices, vertexConsumer, light, overlay);
            } else {
                Identifier textureId = Identifier.of("skystone", "entity/meteorite_iron_shield_base_no_pattern");
                SpriteIdentifier spriteIdentifier = new SpriteIdentifier(
                        TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE,
                        textureId
                );

                VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, stack.hasGlint());

                this.modelShield.getHandle().render(matrices, vertexConsumer, light, overlay);

                DyeColor baseColor = stack.getOrDefault(DataComponentTypes.BASE_COLOR, DyeColor.WHITE);

                BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay,
                        this.modelShield.getPlate(), spriteIdentifier, false, baseColor, patterns, stack.hasGlint());
            }

            matrices.pop();
            ci.cancel();
        }
    }
}