package amedonai.ss_celestial_threat;

import amedonai.ss_celestial_threat.entity.SkyStoneFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
        public static final BlockEntityType<SkyStoneFurnaceBlockEntity> SKY_STONE_FURNACE_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of(SkyStoneCelestialThreat.MOD_ID, "sky_stone_furnace"),
            BlockEntityType.Builder.create(SkyStoneFurnaceBlockEntity::new, ModBlocks.SKY_STONE_FURNACE).build(null)
    );

    public static void initialize() {
            }
}