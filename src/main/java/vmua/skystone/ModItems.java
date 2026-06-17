package vmua.skystone;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.fluid.Fluids;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ModItems {

    // Метеоритное железо
    public static final Item METEORITE_IRON_INGOT = registerItem("meteorite_iron_ingot",
            new Item(new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_NUGGET = registerItem("meteorite_iron_nugget",
            new Item(generalSettings()));

    // Инструменты из метеоритного железа
    public static final Item METEORITE_IRON_SWORD = registerItem("meteorite_iron_sword",
            new SwordItem(ModToolMaterial.METEORITE_IRON, 3, -2.4f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_PICKAXE = registerItem("meteorite_iron_pickaxe",
            new PickaxeItem(ModToolMaterial.METEORITE_IRON, 1, -2.8f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {});

    public static final Item METEORITE_IRON_AXE = registerItem("meteorite_iron_axe",
            new AxeItem(ModToolMaterial.METEORITE_IRON, 6.0f, -3.1f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {});

    public static final Item METEORITE_IRON_SHOVEL = registerItem("meteorite_iron_shovel",
            new ShovelItem(ModToolMaterial.METEORITE_IRON, 1.5f, -3.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_HOE = registerItem("meteorite_iron_hoe",
            new HoeItem(ModToolMaterial.METEORITE_IRON, -3, 0.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {});

    // МЕТЕОРИТНЫЙ ЩИТ (Прочность 840 = 336 * 2.5)
    public static final Item METEORITE_IRON_SHIELD = registerItem("meteorite_iron_shield",
            new MeteoriteIronShieldItem(new FabricItemSettings().maxDamage(840).group(SkyStone.SKYSTONE_GROUP)));

    // Броня из метеоритного железа
    public static final Item METEORITE_IRON_HELMET = registerItem("meteorite_iron_helmet",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.HEAD, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_CHESTPLATE = registerItem("meteorite_iron_chestplate",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.CHEST, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_LEGGINGS = registerItem("meteorite_iron_leggings",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.LEGS, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item METEORITE_IRON_BOOTS = registerItem("meteorite_iron_boots",
            new MeteoriteArmorItem(ModArmorMaterial.METEORITE_IRON, EquipmentSlot.FEET, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    // Общие настройки для обычных предметов (стакаются до 64)
    private static FabricItemSettings generalSettings() {
        return new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP);
    }

    // Настройки для пустых вёдер (стакаются до 16)
    private static FabricItemSettings emptyBucketSettings() {
        return new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP).maxCount(16);
    }

    // Настройки для полных вёдер (НЕ стакаются, максимум 1)
    private static FabricItemSettings filledBucketSettings() {
        return new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP).maxCount(1);
    }

    // ЗОЛОТЫЕ ВЁДРА
    public static final Item GOLDEN_BUCKET = registerItem("golden_bucket",
            new ModEmptyBucketItem(() -> ModItems.GOLDEN_WATER_BUCKET, () -> ModItems.GOLDEN_LAVA_BUCKET, emptyBucketSettings()));

    public static final Item GOLDEN_WATER_BUCKET = registerItem("golden_water_bucket",
            new ModFilledBucketItem(Fluids.WATER, () -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    public static final Item GOLDEN_LAVA_BUCKET = registerItem("golden_lava_bucket",
            new ModFilledBucketItem(Fluids.LAVA, () -> ModItems.GOLDEN_BUCKET, filledBucketSettings()));

    public static final Item GOLDEN_MILK_BUCKET = registerItem("golden_milk_bucket",
            new Item(filledBucketSettings()));


    // МЕТЕОРИТНЫЕ ВЁДРА
    public static final Item METEORITE_IRON_BUCKET = registerItem("meteorite_iron_bucket",
            new ModEmptyBucketItem(() -> ModItems.METEORITE_IRON_WATER_BUCKET, () -> ModItems.METEORITE_IRON_LAVA_BUCKET, emptyBucketSettings()));

    public static final Item METEORITE_IRON_WATER_BUCKET = registerItem("meteorite_iron_water_bucket",
            new ModFilledBucketItem(Fluids.WATER, () -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    public static final Item METEORITE_IRON_LAVA_BUCKET = registerItem("meteorite_iron_lava_bucket",
            new ModFilledBucketItem(Fluids.LAVA, () -> ModItems.METEORITE_IRON_BUCKET, filledBucketSettings()));

    public static final Item METEORITE_IRON_MILK_BUCKET = registerItem("meteorite_iron_milk_bucket",
            new Item(filledBucketSettings()));

    // Небесный камень (Инструменты)
    public static final Item SKY_STONE_SWORD = registerItem("sky_stone_sword",
            new SwordItem(ModToolMaterial.SKY_STONE, 3, -2.4f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item SKY_STONE_PICKAXE = registerItem("sky_stone_pickaxe",
            new PickaxeItem(ModToolMaterial.SKY_STONE, 1, -2.8f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {});

    public static final Item SKY_STONE_AXE = registerItem("sky_stone_axe",
            new AxeItem(ModToolMaterial.SKY_STONE, 7.0f, -3.2f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {});

    public static final Item SKY_STONE_SHOVEL = registerItem("sky_stone_shovel",
            new ShovelItem(ModToolMaterial.SKY_STONE, 1.5f, -3.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));

    public static final Item SKY_STONE_HOE = registerItem("sky_stone_hoe",
            new HoeItem(ModToolMaterial.SKY_STONE, -1, -2.0f, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)) {});

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(SkyStone.MOD_ID, name), item);
    }

    public static void initialize() {
    }

    // Класс для нашего метеоритного щита с поддержкой ремонта
    public static class MeteoriteIronShieldItem extends ShieldItem {
        public MeteoriteIronShieldItem(Settings settings) {
            super(settings);
        }

        @Override
        public boolean canRepair(ItemStack stack, ItemStack ingredient) {
            // Позволяет чинить щит метеоритными слитками
            return ingredient.getItem() == ModItems.METEORITE_IRON_INGOT || super.canRepair(stack, ingredient);
        }
    }

    // Класс для метеоритной брони
    public static class MeteoriteArmorItem extends ArmorItem {
        private static final Map<UUID, Double> FALL_START_Y = new HashMap<>();

        public MeteoriteArmorItem(ArmorMaterial material, EquipmentSlot slot, Settings settings) {
            super(material, slot, settings);
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