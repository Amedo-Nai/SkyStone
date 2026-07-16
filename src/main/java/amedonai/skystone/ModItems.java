package amedonai.skystone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModItems {

    // Инструменты из небесного камня
    public static final Item SKY_STONE_SWORD = registerItem("sky_stone_sword",
            new SwordItem(ModToolMaterial.SKY_STONE, 3, -2.4f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item SKY_STONE_PICKAXE = registerItem("sky_stone_pickaxe",
            new PickaxeItem(ModToolMaterial.SKY_STONE, 1, -2.8f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item SKY_STONE_AXE = registerItem("sky_stone_axe",
            new AxeItem(ModToolMaterial.SKY_STONE, 7.0f, -3.2f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item SKY_STONE_SHOVEL = registerItem("sky_stone_shovel",
            new ShovelItem(ModToolMaterial.SKY_STONE, 1.5f, -3.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item SKY_STONE_HOE = registerItem("sky_stone_hoe",
            new HoeItem(ModToolMaterial.SKY_STONE, -1, -2.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    // Метеоритное железо и связанный контент
    public static final Item METEORITE_IRON_INGOT = registerItem("meteorite_iron_ingot",
            new Item(new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item METEORITE_IRON_NUGGET = registerItem("meteorite_iron_nugget",
            new Item(generalSettings()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    // Инструменты из метеоритного железа
    public static final Item METEORITE_IRON_SWORD = registerItem("meteorite_iron_sword",
            new SwordItem(ModToolMaterial.METEORITE_IRON, 3, -2.4f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item METEORITE_IRON_PICKAXE = registerItem("meteorite_iron_pickaxe",
            new PickaxeItem(ModToolMaterial.METEORITE_IRON, 1, -2.8f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip_extra", Formatting.DARK_PURPLE);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item METEORITE_IRON_AXE = registerItem("meteorite_iron_axe",
            new AxeItem(ModToolMaterial.METEORITE_IRON, 6.0f, -3.1f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item METEORITE_IRON_SHOVEL = registerItem("meteorite_iron_shovel",
            new ShovelItem(ModToolMaterial.METEORITE_IRON, 1.5f, -3.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item METEORITE_IRON_HOE = registerItem("meteorite_iron_hoe",
            new HoeItem(ModToolMaterial.METEORITE_IRON, -3, 0.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, context);
                }
            });

    public static final Item METEORITE_IRON_SHIELD = registerItem("meteorite_iron_shield",
            new MeteoriteIronShieldItem(new FabricItemSettings().maxDamage(840).group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_GOLEM_SPAWN_EGG = registerItem("meteorite_iron_golem_spawn_egg",
            new SpawnEggItem(ModEntities.METEORITE_IRON_GOLEM, 0xf4d6aa, 0x87603a, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    // Броня из метеоритного железа
    public static final Item METEORITE_IRON_HELMET = registerItem("meteorite_iron_helmet",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.HEAD, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_CHESTPLATE = registerItem("meteorite_iron_chestplate",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.CHEST, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_LEGGINGS = registerItem("meteorite_iron_leggings",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.LEGS, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_BOOTS = registerItem("meteorite_iron_boots",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.FEET, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    // Золотые вёдра
    public static final Item GOLDEN_BUCKET = registerItem("golden_bucket",
            new ModEmptyBucketItem(() -> ModItems.GOLDEN_WATER_BUCKET, () -> ModItems.GOLDEN_LAVA_BUCKET, emptyBucketSettings()));

    public static final Item GOLDEN_WATER_BUCKET = registerItem("golden_water_bucket",
            new ModFilledBucketItem(Fluids.WATER, () -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    public static final Item GOLDEN_LAVA_BUCKET = registerItem("golden_lava_bucket",
            new ModFilledBucketItem(Fluids.LAVA, () -> ModItems.GOLDEN_BUCKET,
                    filledBucketSettings().recipeRemainder(ModItems.GOLDEN_BUCKET)));

    public static final Item GOLDEN_MILK_BUCKET = registerItem("golden_milk_bucket",
            new ModMilkBucketItem(() -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    public static final Item GOLDEN_COD_BUCKET = registerItem("golden_cod_bucket",
            new ModFishBucketItem(EntityType.COD, () -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    public static final Item GOLDEN_SALMON_BUCKET = registerItem("golden_salmon_bucket",
            new ModFishBucketItem(EntityType.SALMON, () -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    public static final Item GOLDEN_PUFFERFISH_BUCKET = registerItem("golden_pufferfish_bucket",
            new ModFishBucketItem(EntityType.PUFFERFISH, () -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    public static final Item GOLDEN_TROPICAL_FISH_BUCKET = registerItem("golden_tropical_fish_bucket",
            new ModFishBucketItem(EntityType.TROPICAL_FISH, () -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    // Вёдра из метеоритного железа
    public static final Item METEORITE_IRON_BUCKET = registerItem("meteorite_iron_bucket",
            new ModEmptyBucketItem(() -> ModItems.METEORITE_IRON_WATER_BUCKET, () -> ModItems.METEORITE_IRON_LAVA_BUCKET, emptyBucketSettings()));

    public static final Item METEORITE_IRON_WATER_BUCKET = registerItem("meteorite_iron_water_bucket",
            new ModFilledBucketItem(Fluids.WATER, () -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    public static final Item METEORITE_IRON_LAVA_BUCKET = registerItem("meteorite_iron_lava_bucket",
            new ModFilledBucketItem(Fluids.LAVA, () -> ModItems.METEORITE_IRON_BUCKET,
                    filledBucketSettings().recipeRemainder(ModItems.METEORITE_IRON_BUCKET)));

    public static final Item METEORITE_IRON_MILK_BUCKET = registerItem("meteorite_iron_milk_bucket",
            new ModMilkBucketItem(() -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    public static final Item METEORITE_IRON_COD_BUCKET = registerItem("meteorite_iron_cod_bucket",
            new ModFishBucketItem(EntityType.COD, () -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    public static final Item METEORITE_IRON_SALMON_BUCKET = registerItem("meteorite_iron_salmon_bucket",
            new ModFishBucketItem(EntityType.SALMON, () -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    public static final Item METEORITE_IRON_PUFFERFISH_BUCKET = registerItem("meteorite_iron_pufferfish_bucket",
            new ModFishBucketItem(EntityType.PUFFERFISH, () -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    public static final Item METEORITE_IRON_TROPICAL_FISH_BUCKET = registerItem("meteorite_iron_tropical_fish_bucket",
            new ModFishBucketItem(EntityType.TROPICAL_FISH, () -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    // Вспомогательные методы и настройки
    private static FabricItemSettings generalSettings() {
        return new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP);
    }

    private static FabricItemSettings emptyBucketSettings() {
        return new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP).maxCount(16);
    }

    private static FabricItemSettings filledBucketSettings() {
        return new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP).maxCount(1);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SkyStone.MOD_ID, name), item);
    }

    public static void initialize() {
    }

    // Кастомные классы предметов (Внутренняя логика)
    public static class MeteoriteIronShieldItem extends ShieldItem {
        public MeteoriteIronShieldItem(Settings settings) {
            super(settings);
        }

        @Override
        @Environment(EnvType.CLIENT)
        public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
            TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
            super.appendTooltip(stack, world, tooltip, context);
        }

        @Override
        public boolean canRepair(ItemStack stack, ItemStack ingredient) {
            return ingredient.getItem() == ModItems.METEORITE_IRON_INGOT || super.canRepair(stack, ingredient);
        }
    }

    public static class MeteoriteArmorItem extends ArmorItem {
        private static final Map<UUID, Double> FALL_START_Y = new HashMap<>();

        public MeteoriteArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
            super(material, slot, settings);
        }

        @Override
        @Environment(EnvType.CLIENT)
        public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
            TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
            super.appendTooltip(stack, world, tooltip, context);
        }

        @Override
        public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
            if (!world.isClient() && entity instanceof PlayerEntity && this.slot == EquipmentSlot.CHEST) {
                PlayerEntity player = (PlayerEntity) entity;

                if (hasFullSetOfMeteoriteArmor(player)) {
                    if (player.fallDistance == 0) {
                        FALL_START_Y.put(player.getUuid(), player.getY());
                    } else {
                        double startY = FALL_START_Y.getOrDefault(player.getUuid(), player.getY());
                        double actualDrop = startY - player.getY();

                        if (actualDrop > 0) {
                            player.fallDistance = (float) (actualDrop * 0.21f);
                        }
                    }
                } else {
                    FALL_START_Y.remove(player.getUuid());
                }
            }
            super.inventoryTick(stack, world, entity, slot, selected);
        }

        private boolean hasFullSetOfMeteoriteArmor(PlayerEntity player) {
            ItemStack helmet = player.getEquippedStack(EquipmentSlot.HEAD);
            ItemStack chestplate = player.getEquippedStack(EquipmentSlot.CHEST);
            ItemStack leggings = player.getEquippedStack(EquipmentSlot.LEGS);
            ItemStack boots = player.getEquippedStack(EquipmentSlot.FEET);

            return isMeteoriteArmorPiece(helmet) && isMeteoriteArmorPiece(chestplate)
                    && isMeteoriteArmorPiece(leggings) && isMeteoriteArmorPiece(boots);
        }

        private boolean isMeteoriteArmorPiece(ItemStack stack) {
            if (stack.isEmpty() || !(stack.getItem() instanceof ArmorItem)) {
                return false;
            }
            return ((ArmorItem) stack.getItem()).getMaterial() == ModArmorMaterial.METEORITE_IRON;
        }
    }
}