package amedonai.ss_celestial_threat.client.mixin;

import amedonai.ss_celestial_threat.ModItems; // Путь к твоим новым предметам
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    // В 1.21.1 Yarn поле называется modelShield
    @Shadow @Final
    private ShieldEntityModel modelShield;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderMeteoriteIronShield(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {

        // Проверяем твой щит через современный метод .isOf()
        if (stack.isOf(ModItems.METEORITE_IRON_SHIELD)) {

            matrices.push();

            // Стандартный разворот матрицы щита, чтобы он не висел вверх ногами
            matrices.scale(1.0F, -1.0F, -1.0F);

            // Путь к текстуре в 1.21.1 (через Identifier.of)
            Identifier rawTextureId = Identifier.of("skystone-celestial-threat", "textures/entity/meteorite_iron_shield_base.png");
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(rawTextureId));

            this.modelShield.render(matrices, vertexConsumer, light, overlay, 0xFFFFFFFF);

            matrices.pop();

            // Отменяем стандартный рендер Майнкрафта
            ci.cancel();
        }
    }
}