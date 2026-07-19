package amedonai.skystone.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import amedonai.skystone.AdvancementHelper;
import amedonai.skystone.SkyStone;
import amedonai.skystone.entity.MeteoriteIronGolemEntity;
import java.util.UUID;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Unique
    private boolean skystone$earnedGravity;

    @Unique
    private double skystone$maxFallStartY;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;

            if (player.isOnGround() || player.isTouchingWater() || player.isSubmergedInWater()) {
                this.skystone$maxFallStartY = player.getY();
            } else {
                if (player.getY() > this.skystone$maxFallStartY) {
                    this.skystone$maxFallStartY = player.getY();
                }
            }
        }
    }

    @Inject(method = "handleFallDamage", at = @At("HEAD"))
    private void onHandleFallDamageHead(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;

            boolean hasFullSet = SkyStone.hasFullMeteoriteArmor(player);
            boolean isWet = player.isTouchingWater() || player.isSubmergedInWater();

            double actualDropDistance = this.skystone$maxFallStartY - player.getY();

            if (actualDropDistance >= 100.0 && hasFullSet && !isWet) {
                this.skystone$earnedGravity = true;
            } else {
                this.skystone$earnedGravity = false;
            }
        }
    }

    @Inject(method = "handleFallDamage", at = @At("RETURN"))
    private void onHandleFallDamageReturn(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;

            if (this.skystone$earnedGravity && player.isAlive() && player.getHealth() > 0.0F) {
                AdvancementHelper.grantAdvancement(player, "meteorite_gravity");
            }

            this.skystone$earnedGravity = false;
        }
    }

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