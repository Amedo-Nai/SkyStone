package amedonai.ss_celestial_threat.client.render.feature;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.Cracks;
import net.minecraft.util.Identifier;
import amedonai.ss_celestial_threat.entity.MeteoriteIronGolemEntity;

@Environment(EnvType.CLIENT)
public class MeteoriteIronGolemCrackFeatureRenderer extends FeatureRenderer<MeteoriteIronGolemEntity, IronGolemEntityModel<MeteoriteIronGolemEntity>> {
    private static final Map<Cracks.CrackLevel, Identifier> DAMAGE_TO_TEXTURE;

    public MeteoriteIronGolemCrackFeatureRenderer(FeatureRendererContext<MeteoriteIronGolemEntity, IronGolemEntityModel<MeteoriteIronGolemEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, MeteoriteIronGolemEntity entity, float f, float g, float h, float j, float k, float l) {
        if (!entity.isInvisible()) {
            Cracks.CrackLevel crackLevel = entity.getCrackLevel();
            if (crackLevel != Cracks.CrackLevel.NONE) {
                Identifier identifier = DAMAGE_TO_TEXTURE.get(crackLevel);
                renderModel(this.getContextModel(), identifier, matrixStack, vertexConsumerProvider, i, entity, 0xFFFFFFFF);
            }
        }
    }

    static {
        DAMAGE_TO_TEXTURE = ImmutableMap.of(
                Cracks.CrackLevel.LOW, Identifier.of("skystone", "textures/entity/golem/meteorite_iron_golem_crackiness_low.png"),
                Cracks.CrackLevel.MEDIUM, Identifier.of("skystone", "textures/entity/golem/meteorite_iron_golem_crackiness_medium.png"),
                Cracks.CrackLevel.HIGH, Identifier.of("skystone", "textures/entity/golem/meteorite_iron_golem_crackiness_high.png")
        );
    }
}