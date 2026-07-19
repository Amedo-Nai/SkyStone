package amedonai.skystone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TallBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import amedonai.skystone.block.MeteoriteIronPressurePlateBlock;
import amedonai.skystone.block.SkyStoneFurnaceBlock;

import java.util.List;

public class ModBlocks {

    // Метеоритные блоки (Требуют железную кирку — Уровень 2)
    public static final Block METEORITE_IRON_ORE = registerBlock("meteorite_iron_ore",
            new Block(FabricBlockSettings.of(Material.STONE)
                    .hardness(3.0f)
                    .resistance(3.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()));

    public static final Block METEORITE_IRON_BLOCK = registerBlock("meteorite_iron_block",
            new Block(FabricBlockSettings.of(Material.METAL)
                    .hardness(5.0f)
                    .resistance(6.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    // Нажимная плита (Требует железную кирку — Уровень 2)
    public static final Block METEORITE_IRON_PRESSURE_PLATE = registerBlock("meteorite_iron_pressure_plate",
            new MeteoriteIronPressurePlateBlock(PressurePlateBlock.ActivationRule.EVERYTHING,
                    FabricBlockSettings.of(Material.METAL)
                            .hardness(5.0f)
                            .resistance(6.0f)
                            .breakByTool(FabricToolTags.PICKAXES, 2)
                            .requiresTool()
                            .sounds(BlockSoundGroup.METAL)
                            .noCollision()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    public static final Block METEORITE_IRON_DOOR = registerDoor("meteorite_iron_door",
            new ModDoorBlock(FabricBlockSettings.of(Material.METAL)
                    .hardness(5.0f)
                    .resistance(6.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()));

    public static final Block METEORITE_IRON_TRAPDOOR = registerBlock("meteorite_iron_trapdoor",
            new ModTrapdoorBlock(FabricBlockSettings.of(Material.METAL)
                    .hardness(5.0f)
                    .resistance(6.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()));

    // Золотые наковальни (Требуют железную кирку — Уровень 2)
    public static final Block GOLDEN_ANVIL = registerBlock("golden_anvil",
            new AnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).breakByTool(FabricToolTags.PICKAXES, 2).requiresTool()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    public static final Block CHIPPED_GOLDEN_ANVIL = registerBlock("chipped_golden_anvil",
            new AnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).breakByTool(FabricToolTags.PICKAXES, 2).requiresTool()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    public static final Block DAMAGED_GOLDEN_ANVIL = registerBlock("damaged_golden_anvil",
            new AnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).breakByTool(FabricToolTags.PICKAXES, 2).requiresTool()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    // Метеоритные наковальни (Требуют железную кирку — Уровень 2)
    public static final Block METEORITE_IRON_ANVIL = registerBlock("meteorite_iron_anvil",
            new AnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).hardness(6.5f).resistance(1500.0f).breakByTool(FabricToolTags.PICKAXES, 2).requiresTool()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    public static final Block CHIPPED_METEORITE_IRON_ANVIL = registerBlock("chipped_meteorite_iron_anvil",
            new AnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).hardness(6.5f).resistance(1500.0f).breakByTool(FabricToolTags.PICKAXES, 2).requiresTool()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    public static final Block DAMAGED_METEORITE_IRON_ANVIL = registerBlock("damaged_meteorite_iron_anvil",
            new AnvilBlock(FabricBlockSettings.copyOf(Blocks.ANVIL).hardness(6.5f).resistance(1500.0f).breakByTool(FabricToolTags.PICKAXES, 2).requiresTool()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    // Золотые блоки (Дверь и Люк) (Требуют железную кирку — Уровень 2)
    public static final Block GOLDEN_DOOR = registerDoor("golden_door",
            new ModDoorBlock(FabricBlockSettings.of(Material.METAL)
                    .hardness(3.0f)
                    .resistance(3.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()));

    public static final Block GOLDEN_TRAPDOOR = registerBlock("golden_trapdoor",
            new ModTrapdoorBlock(FabricBlockSettings.of(Material.METAL)
                    .hardness(3.0f)
                    .resistance(3.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()));

    // Метеоритные и золотые решётки (Требуют железную кирку — Уровень 2)
    public static final Block METEORITE_IRON_BARS = registerBlock("meteorite_iron_bars",
            new ModPaneBlock(FabricBlockSettings.of(Material.METAL)
                    .hardness(5.0f)
                    .resistance(6.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()));

    public static final Block GOLDEN_BARS = registerBlock("golden_bars",
            new ModPaneBlock(FabricBlockSettings.of(Material.METAL)
                    .hardness(3.0f)
                    .resistance(3.0f)
                    .breakByTool(FabricToolTags.PICKAXES, 2)
                    .requiresTool()
                    .sounds(BlockSoundGroup.METAL)
                    .nonOpaque()));

    // Небесный камень (Базовый уровень 0 — добывается деревом/золотом)
    public static final Block SKY_STONE = registerBlock("sky_stone",
            new Block(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()) {
                @Override
                @Environment(EnvType.CLIENT)
                public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, world, tooltip, options);
                }
            });

    public static final Block SKY_COBBLESTONE = registerBlock("sky_cobblestone",
            new Block(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SKY_STONE_BRICKS = registerBlock("sky_stone_bricks",
            new Block(FabricBlockSettings.of(Material.STONE).hardness(3.0f).resistance(3.6f).requiresTool()));

    public static final Block SMOOTH_SKY_STONE = registerBlock("smooth_sky_stone",
            new Block(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SMOOTH_SKY_STONE_SLAB = registerBlock("smooth_sky_stone_slab",
            new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SKY_STONE_SLAB = registerBlock("sky_stone_slab",
            new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SKY_STONE_STAIRS = registerBlock("sky_stone_stairs",
            new ModStairsBlock(ModBlocks.SKY_STONE.getDefaultState(), FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SKY_STONE_WALL = registerBlock("sky_stone_wall",
            new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block CRACKED_SKY_STONE_BRICKS = registerBlock("cracked_sky_stone_bricks",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS).requiresTool()));

    public static final Block CHISELED_SKY_STONE_BRICKS = registerBlock("chiseled_sky_stone_bricks",
            new Block(FabricBlockSettings.copyOf(Blocks.STONE_BRICKS).requiresTool()));

    // Smooth Sky Stone варианты
    public static final Block SMOOTH_SKY_STONE_STAIRS = registerBlock("smooth_sky_stone_stairs",
            new ModStairsBlock(ModBlocks.SMOOTH_SKY_STONE.getDefaultState(), FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SMOOTH_SKY_STONE_WALL = registerBlock("smooth_sky_stone_wall",
            new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    // Sky Cobblestone варианты
    public static final Block SKY_COBBLESTONE_SLAB = registerBlock("sky_cobblestone_slab",
            new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SKY_COBBLESTONE_STAIRS = registerBlock("sky_cobblestone_stairs",
            new ModStairsBlock(ModBlocks.SKY_COBBLESTONE.getDefaultState(), FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    public static final Block SKY_COBBLESTONE_WALL = registerBlock("sky_cobblestone_wall",
            new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(2.4f).resistance(2.4f).requiresTool()));

    // Sky Stone Bricks варианты
    public static final Block SKY_STONE_BRICKS_SLAB = registerBlock("sky_stone_bricks_slab",
            new SlabBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).resistance(3.6f).requiresTool()));

    public static final Block SKY_STONE_BRICKS_STAIRS = registerBlock("sky_stone_bricks_stairs",
            new ModStairsBlock(ModBlocks.SKY_STONE_BRICKS.getDefaultState(), FabricBlockSettings.of(Material.STONE).hardness(3.0f).resistance(3.6f).requiresTool()));

    public static final Block SKY_STONE_BRICKS_WALL = registerBlock("sky_stone_bricks_wall",
            new WallBlock(FabricBlockSettings.of(Material.STONE).hardness(3.0f).resistance(3.6f).requiresTool()));

    public static final Block SKY_STONE_FURNACE = registerBlock("sky_stone_furnace",
            new SkyStoneFurnaceBlock(FabricBlockSettings.of(Material.STONE)
                    .hardness(3.5f)
                    .resistance(3.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .luminance(state -> state.get(AbstractFurnaceBlock.LIT) ? 13 : 0)));

    // Кнопка
    public static final Block SKY_STONE_BUTTON = registerBlock("sky_stone_button",
            new StoneButtonBlock(FabricBlockSettings.of(Material.DECORATION)
                    .hardness(0.5f)
                    .resistance(0.5f)
                    .sounds(BlockSoundGroup.STONE)) {});

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registry.BLOCK, new Identifier(SkyStone.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registry.ITEM, new Identifier(SkyStone.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));
    }

    private static Block registerDoor(String name, Block block) {
        Registry.register(Registry.ITEM, new Identifier(SkyStone.MOD_ID, name),
                new TallBlockItem(block, new FabricItemSettings().group(SkyStone.SKYSTONE_GROUP)));
        return Registry.register(Registry.BLOCK, new Identifier(SkyStone.MOD_ID, name), block);
    }

    public static class ModStairsBlock extends StairsBlock {
        public ModStairsBlock(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
    }

    public static class ModDoorBlock extends DoorBlock {
        public ModDoorBlock(Settings settings) {
            super(settings);
        }
    }

    public static class ModPaneBlock extends PaneBlock {
        public ModPaneBlock(Settings settings) {
            super(settings);
        }
    }

    public static class ModTrapdoorBlock extends TrapdoorBlock {
        public ModTrapdoorBlock(Settings settings) {
            super(settings);
        }
    }

    public static void initialize() {
    }
}