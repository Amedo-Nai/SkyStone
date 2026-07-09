package vmua.skystone.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import vmua.skystone.ModItems;

public class MeteoriteIronGolemEntity extends IronGolemEntity {

    public MeteoriteIronGolemEntity(EntityType<? extends IronGolemEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createMeteoriteGolemAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0D) // ХП чуть больше ванильного (200 вместо 100)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 20.0D); // Базовый урон больше (20 вместо 15)
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.add(2, new WanderNearTargetGoal(this, 0.9D, 32.0F));
        this.goalSelector.add(2, new WanderAroundPointOfInterestGoal(this, 0.6D, false));
        this.goalSelector.add(4, new IronGolemWanderAroundGoal(this, 0.6D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(1, new TrackIronGolemTargetGoal(this));

        this.targetSelector.add(2, new RevengeGoal(this));

        this.targetSelector.add(3, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, MobEntity.class, 5, false, false, (livingEntity) -> livingEntity instanceof Monster && !(livingEntity instanceof CreeperEntity)));
        this.targetSelector.add(3, new FollowTargetGoal<>(this, CreeperEntity.class, 5, false, false, null));
        this.targetSelector.add(4, new UniversalAngerGoal(this, false));
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        // Если голем построен игроком вручную — он не трогает игрока вообще
        if (this.isPlayerCreated() && type == EntityType.PLAYER) {
            return false;
        }
        // Разрешаем атаковать криперов
        if (type == EntityType.CREEPER) {
            return true;
        }
        return super.canTarget(type);
    }

    @Override
    public boolean tryAttack(Entity target) {
        this.world.sendEntityStatus(this, (byte) 4);

        float damageAmount;
        if (target instanceof CreeperEntity) {
            damageAmount = 1000000000.0F; // Ваншот для криперов
        } else {
            float baseDamage = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            damageAmount = (int) baseDamage > 0 ? baseDamage / 2.0F + (float) this.random.nextInt((int) baseDamage) : baseDamage;
        }

        boolean hasDealtDamage = target.damage(DamageSource.mob(this), damageAmount);

        if (hasDealtDamage) {
            target.setVelocity(target.getVelocity().add(0.0D, 0.4D, 0.0D));
            this.dealDamage(this, target);
        }

        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        return hasDealtDamage;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 4) {
            this.playSound(SoundEvents.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F);
        }
        super.handleStatus(status);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        float healAmount = 0.0F;

        // Проверяем, чем именно кликнул игрок
        if (itemStack.getItem() == ModItems.METEORITE_IRON_INGOT) {
            healAmount = 50.0F; // Метеоритный слиток хилит 50 ХП
        } else if (itemStack.getItem() == net.minecraft.item.Items.IRON_INGOT) {
            healAmount = 25.0F; // Обычный железный слиток хилит 25 ХП
        }

        // Если в руке не метеорит и не железо — отдаем управление ванильной логике
        if (healAmount == 0.0F) {
            return super.interactMob(player, hand);
        } else {
            float currentHealth = this.getHealth();
            this.heal(healAmount);

            // Если голем уже полностью здоров, предмет не тратится
            if (this.getHealth() == currentHealth) {
                return ActionResult.PASS;
            } else {
                // Воспроизводим звук починки с небольшим случайным изменением тона
                float pitch = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0F, pitch);

                // Забираем предмет, если игрок не в креативе
                if (!player.abilities.creativeMode) {
                    itemStack.decrement(1);
                }
                return ActionResult.success(this.world.isClient);
            }
        }
    }

    @Override
    protected int getXpToDrop(PlayerEntity player) {
        return 12; // Повышенный опыт за убийство
    }
}