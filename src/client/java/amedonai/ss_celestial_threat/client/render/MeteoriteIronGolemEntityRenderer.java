package amedonai.ss_celestial_threat.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.util.Identifier;
import amedonai.ss_celestial_threat.entity.MeteoriteIronGolemEntity;
import amedonai.ss_celestial_threat.client.render.feature.MeteoriteIronGolemCrackFeatureRenderer;

@Environment(EnvType.CLIENT)
public class MeteoriteIronGolemEntityRenderer extends MobEntityRenderer<MeteoriteIronGolemEntity, IronGolemEntityModel<MeteoriteIronGolemEntity>> {
    private static final Identifier TEXTURE = Identifier.of("skystone", "textures/entity/golem/meteorite_iron_golem.png");

    public MeteoriteIronGolemEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new IronGolemEntityModel<>(context.getPart(EntityModelLayers.IRON_GOLEM)), 0.7F);
        this.addFeature(new MeteoriteIronGolemCrackFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(MeteoriteIronGolemEntity entity) {
        return TEXTURE;
    }
}