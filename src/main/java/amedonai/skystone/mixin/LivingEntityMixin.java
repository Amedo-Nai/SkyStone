package amedonai.skystone.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import amedonai.skystone.AdvancementHelper;
import amedonai.skystone.entity.MeteoriteIronGolemEntity;
import java.util.UUID;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onDeath", at = @At("HEAD"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof WitherEntity && !entity.world.isClient()) {

            if (source.getAttacker() instanceof MeteoriteIronGolemEntity) {
                MeteoriteIronGolemEntity golem = (MeteoriteIronGolemEntity) source.getAttacker();
                UUID ownerUuid = golem.getOwnerUuid();

                if (ownerUuid != null && entity.world.getServer() != null) {
                    ServerPlayerEntity owner = entity.world.getServer().getPlayerManager().getPlayer(ownerUuid);

                    if (owner != null) {
                        AdvancementHelper.grantAdvancement(owner, "golem_wither");

                        String command = "advancement grant " + owner.getGameProfile().getName() + " only minecraft:nether/kill_wither";
                        entity.world.getServer().getCommandManager().execute(entity.world.getServer().getCommandSource(), command);
                    }
                }
            }
        }
    }
}