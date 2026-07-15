package amedonai.skystone;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import amedonai.skystone.entity.SkyStoneFurnaceBlockEntity;

public class ModBlockEntities {
    public static BlockEntityType<SkyStoneFurnaceBlockEntity> SKY_STONE_FURNACE_ENTITY;

    public static void registerAllBlockEntities() {
        // Используем строго ванильный билдер, который ты скинул из исходников
        SKY_STONE_FURNACE_ENTITY = Registry.register(
                Registry.BLOCK_ENTITY_TYPE,
                new Identifier(SkyStone.MOD_ID, "sky_stone_furnace_entity"),
                BlockEntityType.Builder.create(SkyStoneFurnaceBlockEntity::new, ModBlocks.SKY_STONE_FURNACE).build(null)
        );
    }
}