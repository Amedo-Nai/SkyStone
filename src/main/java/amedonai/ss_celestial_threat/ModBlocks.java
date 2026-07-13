package amedonai.ss_celestial_threat;

import amedonai.ss_celestial_threat.block.SkyStoneFurnaceBlock;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModBlocks {

    // 1. Метеоритные блоки
    public static final Block METEORITE_IRON_ORE = registerBlock("meteorite_iron_ore",
            new Block(Block.Settings.create().strength(3.0f, 3.0f).requiresTool()));

    public static final Block METEORITE_IRON_BLOCK = registerBlock("meteorite_iron_block",
            new Block(Block.Settings.create().strength(5.0f, 6.0f).requiresTool().sounds(BlockSoundGroup.METAL)) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    // Нажимная плита
    public static final Block METEORITE_IRON_PRESSURE_PLATE = registerBlock("meteorite_iron_pressure_plate",
            new PressurePlateBlock(BlockSetType.IRON,
                    Block.Settings.create().strength(5.0f, 6.0f).sounds(BlockSoundGroup.METAL).noCollision()) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    // Двери и люки
    public static final Block METEORITE_IRON_DOOR = registerBlock("meteorite_iron_door",
            new DoorBlock(BlockSetType.IRON, Block.Settings.create().strength(5.0f, 6.0f).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque()));

    public static final Block METEORITE_IRON_TRAPDOOR = registerBlock("meteorite_iron_trapdoor",
            new TrapdoorBlock(BlockSetType.IRON, Block.Settings.create().strength(5.0f, 6.0f).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque()));

    // 2. Золотые наковальни
    public static final Block GOLDEN_ANVIL = registerBlock("golden_anvil",
            new AnvilBlock(Block.Settings.copy(Blocks.ANVIL)) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Block CHIPPED_GOLDEN_ANVIL = registerBlock("chipped_golden_anvil",
            new AnvilBlock(Block.Settings.copy(Blocks.ANVIL)) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Block DAMAGED_GOLDEN_ANVIL = registerBlock("damaged_golden_anvil",
            new AnvilBlock(Block.Settings.copy(Blocks.ANVIL)) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    // 3. Метеоритные наковальни
    public static final Block METEORITE_IRON_ANVIL = registerBlock("meteorite_iron_anvil",
            new AnvilBlock(Block.Settings.copy(Blocks.ANVIL).strength(6.5f, 1500.0f)) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Block CHIPPED_METEORITE_IRON_ANVIL = registerBlock("chipped_meteorite_iron_anvil",
            new AnvilBlock(Block.Settings.copy(Blocks.ANVIL).strength(6.5f, 1500.0f)) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Block DAMAGED_METEORITE_IRON_ANVIL = registerBlock("damaged_meteorite_iron_anvil",
            new AnvilBlock(Block.Settings.copy(Blocks.ANVIL).strength(6.5f, 1500.0f)) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    // 4. Золотые блоки (Дверь и Люк)
    public static final Block GOLDEN_DOOR = registerBlock("golden_door",
            new DoorBlock(BlockSetType.IRON, Block.Settings.create().strength(3.0f, 3.0f).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque()));

    public static final Block GOLDEN_TRAPDOOR = registerBlock("golden_trapdoor",
            new TrapdoorBlock(BlockSetType.IRON, Block.Settings.create().strength(3.0f, 3.0f).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque()));

    // 5. Метеоритные и золотые решётки
    public static final Block METEORITE_IRON_BARS = registerBlock("meteorite_iron_bars",
            new PaneBlock(Block.Settings.create().strength(5.0f, 6.0f).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque()));

    public static final Block GOLDEN_BARS = registerBlock("golden_bars",
            new PaneBlock(Block.Settings.create().strength(3.0f, 3.0f).requiresTool().sounds(BlockSoundGroup.METAL).nonOpaque()));

    // 6. Небесный камень и его варианты
    public static final Block SKY_STONE = registerBlock("sky_stone",
            new Block(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()) {
                @Override
                public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
                    TooltipHelper.addTooltipLines(tooltip, this.getTranslationKey() + ".tooltip", Formatting.GRAY);
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

    public static final Block SKY_COBBLESTONE = registerBlock("sky_cobblestone",
            new Block(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_STONE_BRICKS = registerBlock("sky_stone_bricks",
            new Block(Block.Settings.create().strength(3.0f, 3.6f).requiresTool()));

    public static final Block SMOOTH_SKY_STONE = registerBlock("smooth_sky_stone",
            new Block(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SMOOTH_SKY_STONE_SLAB = registerBlock("smooth_sky_stone_slab",
            new SlabBlock(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_STONE_SLAB = registerBlock("sky_stone_slab",
            new SlabBlock(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_STONE_STAIRS = registerBlock("sky_stone_stairs",
            new StairsBlock(ModBlocks.SKY_STONE.getDefaultState(), Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_STONE_WALL = registerBlock("sky_stone_wall",
            new WallBlock(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block CRACKED_SKY_STONE_BRICKS = registerBlock("cracked_sky_stone_bricks",
            new Block(Block.Settings.copy(Blocks.STONE_BRICKS).requiresTool()));

    public static final Block CHISELED_SKY_STONE_BRICKS = registerBlock("chiseled_sky_stone_bricks",
            new Block(Block.Settings.copy(Blocks.STONE_BRICKS).requiresTool()));

    public static final Block SMOOTH_SKY_STONE_STAIRS = registerBlock("smooth_sky_stone_stairs",
            new StairsBlock(ModBlocks.SMOOTH_SKY_STONE.getDefaultState(), Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SMOOTH_SKY_STONE_WALL = registerBlock("smooth_sky_stone_wall",
            new WallBlock(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_COBBLESTONE_SLAB = registerBlock("sky_cobblestone_slab",
            new SlabBlock(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_COBBLESTONE_STAIRS = registerBlock("sky_cobblestone_stairs",
            new StairsBlock(ModBlocks.SKY_COBBLESTONE.getDefaultState(), Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_COBBLESTONE_WALL = registerBlock("sky_cobblestone_wall",
            new WallBlock(Block.Settings.create().strength(2.4f, 2.4f).requiresTool()));

    public static final Block SKY_STONE_BRICKS_SLAB = registerBlock("sky_stone_bricks_slab",
            new SlabBlock(Block.Settings.create().strength(3.0f, 3.6f).requiresTool()));

    public static final Block SKY_STONE_BRICKS_STAIRS = registerBlock("sky_stone_bricks_stairs",
            new StairsBlock(ModBlocks.SKY_STONE_BRICKS.getDefaultState(), Block.Settings.create().strength(3.0f, 3.6f).requiresTool()));

    public static final Block SKY_STONE_BRICKS_WALL = registerBlock("sky_stone_bricks_wall",
            new WallBlock(Block.Settings.create().strength(3.0f, 3.6f).requiresTool()));

    // Космическая печь (Логика её тиков и BlockEntity будет переписана позже)
    public static final Block SKY_STONE_FURNACE = registerBlock("sky_stone_furnace",
            new SkyStoneFurnaceBlock(Block.Settings.create()
                    .strength(3.5f, 3.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .luminance(state -> state.get(AbstractFurnaceBlock.LIT) ? 13 : 0)));

    // Кнопка (В 1.21.1 требует BlockSetType и время нажатия в тиках)
    public static final Block SKY_STONE_BUTTON = registerBlock("sky_stone_button",
            new ButtonBlock(BlockSetType.STONE, 20, Block.Settings.create()
                    .strength(0.5f, 0.5f)
                    .sounds(BlockSoundGroup.STONE)));

    // 7. Системные методы регистрации под 1.21.1
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(SkyStoneCelestialThreat.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(SkyStoneCelestialThreat.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void initialize() {
        SkyStoneCelestialThreat.LOGGER.info("Registered Mod Blocks for SkyStone: Celestial Threat");
    }
}