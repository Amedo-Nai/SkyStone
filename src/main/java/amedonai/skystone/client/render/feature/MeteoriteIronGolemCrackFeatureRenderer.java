package amedonai.skystone.client.render.feature;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.IronGolemEntity.Crack;
import net.minecraft.util.Identifier;
import amedonai.skystone.entity.MeteoriteIronGolemEntity;

@Environment(EnvType.CLIENT)
public class MeteoriteIronGolemCrackFeatureRenderer extends FeatureRenderer<MeteoriteIronGolemEntity, IronGolemEntityModel<MeteoriteIronGolemEntity>> {
    private static final Map<Crack, Identifier> DAMAGE_TO_TEXTURE;

    public MeteoriteIronGolemCrackFeatureRenderer(FeatureRendererContext<MeteoriteIronGolemEntity, IronGolemEntityModel<MeteoriteIronGolemEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MeteoriteIronGolemEntity entity, float f, float g, float h, float j, float k, float l) {
        if (!entity.isInvisible()) {
            Crack crack = entity.getCrack();
            if (crack != Crack.NONE) {
                Identifier identifier = DAMAGE_TO_TEXTURE.get(crack);
                renderModel(this.getContextModel(), identifier, matrixStack, vertexConsumerProvider, i, entity, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    static {
        DAMAGE_TO_TEXTURE = ImmutableMap.of(
                Crack.LOW, new Identifier("skystone", "textures/entity/golem/meteorite_iron_golem_crackiness_low.png"),
                Crack.MEDIUM, new Identifier("skystone", "textures/entity/golem/meteorite_iron_golem_crackiness_medium.png"),
                Crack.HIGH, new Identifier("skystone", "textures/entity/golem/meteorite_iron_golem_crackiness_high.png")
        );
    }
}