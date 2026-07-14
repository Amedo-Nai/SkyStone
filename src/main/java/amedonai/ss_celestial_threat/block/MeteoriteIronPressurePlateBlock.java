package amedonai.ss_celestial_threat.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class MeteoriteIronPressurePlateBlock extends PressurePlateBlock {

    public MeteoriteIronPressurePlateBlock(BlockSetType blockSetType, AbstractBlock.Settings settings) {
        super(blockSetType, settings);
    }

    @Override
    protected int getRedstoneOutput(World world, BlockPos pos) {

        Box box = new Box(pos.getX() + 0.0625, pos.getY(), pos.getZ() + 0.0625,
                pos.getX() + 0.9375, pos.getY() + 0.25, pos.getZ() + 0.9375);

        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);

        for (PlayerEntity player : players) {

            ItemStack feetStack = player.getEquippedStack(EquipmentSlot.FEET);

            if (isMeteoriteBoots(feetStack)) {
                return 15;
            }
        }

        return 0;
    }

    private boolean isMeteoriteBoots(ItemStack stack) {
        if (stack.isEmpty()) return false;

        Identifier id = Registries.ITEM.getId(stack.getItem());

        return id.getNamespace().equals("skystone") && id.getPath().equals("meteorite_iron_boots");
    }
}