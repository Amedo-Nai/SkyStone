package vmua.skystone.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.NbtCompound; // Правильный импорт компаунда для Yarn 1.16.5
import net.minecraft.nbt.NbtList;     // Правильный импорт списка для Yarn 1.16.5
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vmua.skystone.ModItems;

import java.util.List;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    @Shadow @Final
    private ShieldEntityModel modelShield;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderMeteoriteIronShield(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        if (stack.getItem() == ModItems.METEORITE_IRON_SHIELD) {
            NbtCompound blockEntityTag = stack.getSubTag("BlockEntityTag");
            boolean hasBanner = blockEntityTag != null;

            matrices.push();
            matrices.scale(1.0F, -1.0F, -1.0F);

            if (!hasBanner) {
                // Путь: assets/skystone/textures/entity/meteorite_iron_shield_base.png
                Identifier rawTextureId = new Identifier("skystone", "textures/entity/meteorite_iron_shield_base.png");
                VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(rawTextureId));

                // Рендерим чистый щит
                this.modelShield.getHandle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
                this.modelShield.getPlate().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                // Если баннер есть, работаем через зарегистрированный Спрайт в Атласе
                Identifier textureId = new Identifier("skystone", "entity/meteorite_iron_shield_base_no_pattern");
                SpriteIdentifier spriteIdentifier = new SpriteIdentifier(
                        TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE,
                        textureId
                );
                VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);

                this.modelShield.getHandle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

                NbtList nbtList = blockEntityTag.getList("Patterns", 10);
                List<Pair<BannerPattern, DyeColor>> patterns = BannerBlockEntity.method_24280(
                        ShieldItem.getColor(stack),
                        nbtList
                );

                BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, this.modelShield.getPlate(), spriteIdentifier, true, patterns, stack.hasGlint());
            }

            matrices.pop();
            ci.cancel();
        }
    }
}
