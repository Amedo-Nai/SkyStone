package amedonai.ss_celestial_threat;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import amedonai.ss_celestial_threat.ModBlocks;

import java.util.function.Supplier;

public enum ModToolMaterial implements ToolMaterial {
    // Метеоритное железо (уровень Алмаза = 3) -> INCORRECT_FOR_DIAMOND_TOOL
    METEORITE_IRON(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 600, 7.0f, 3.0f, 20, () -> Ingredient.ofItems(ModItems.METEORITE_IRON_INGOT)),
    // Скайстоун (уровень Камня = 1) -> INCORRECT_FOR_STONE_TOOL
    SKY_STONE(BlockTags.INCORRECT_FOR_STONE_TOOL, 200, 5.0f, 1.0f, 15, () -> Ingredient.ofItems(ModBlocks.SKY_STONE));

    private final TagKey<Block> inverseTag;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    ModToolMaterial(TagKey<Block> inverseTag, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.inverseTag = inverseTag;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurability() {
        return this.itemDurability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    @Override
    public float getAttackDamage() {
        return this.attackDamage;
    }

    @Override
    public TagKey<Block> getInverseTag() {
        return this.inverseTag;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
