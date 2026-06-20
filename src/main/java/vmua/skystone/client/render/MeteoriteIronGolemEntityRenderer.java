package vmua.skystone.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.util.Identifier;
import vmua.skystone.entity.MeteoriteIronGolemEntity;
import vmua.skystone.client.render.feature.MeteoriteIronGolemCrackFeatureRenderer;

@Environment(EnvType.CLIENT)
public class MeteoriteIronGolemEntityRenderer extends MobEntityRenderer<MeteoriteIronGolemEntity, IronGolemEntityModel<MeteoriteIronGolemEntity>> {
    private static final Identifier TEXTURE = new Identifier("skystone", "textures/entity/golem/meteorite_iron_golem.png");

    public MeteoriteIronGolemEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new IronGolemEntityModel<>(), 0.7F);
        this.addFeature(new MeteoriteIronGolemCrackFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(MeteoriteIronGolemEntity entity) {
        return TEXTURE;
    }
}