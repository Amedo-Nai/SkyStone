package amedonai.skystone.block;

import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class MeteoriteIronPressurePlateBlock extends PressurePlateBlock {

    public MeteoriteIronPressurePlateBlock(ActivationRule type, Settings settings) {
        super(type, settings);
    }

    @Override
    protected int getRedstoneOutput(World world, BlockPos pos) {
        // Оставляем твою точную невидимую зону проверки
        Box box = new Box(pos.getX() + 0.0625, pos.getY(), pos.getZ() + 0.0625,
                pos.getX() + 0.9375, pos.getY() + 0.25, pos.getZ() + 0.9375);

        // Ищем только игроков в этой зоне (чтобы мобы с предметами не ломали логику замка)
        List<PlayerEntity> players = world.getNonSpectatingEntities(PlayerEntity.class, box);

        for (PlayerEntity player : players) {
            // Проверяем строго слот ног (сапоги)
            ItemStack feetStack = player.getEquippedStack(EquipmentSlot.FEET);

            if (isMeteoriteBoots(feetStack)) {
                return 15; // Клик! Доступ разрешен
            }
        }

        return 0;
    }

    // Проверяем, что на ногах именно метеоритные сапоги
    private boolean isMeteoriteBoots(ItemStack stack) {
        if (stack.isEmpty()) return false;

        Identifier id = Registry.ITEM.getId(stack.getItem());

        // Проверяем namespace и точное совпадение имени предмета сапог
        return id.getNamespace().equals("skystone") && id.getPath().equals("meteorite_iron_boots");
    }
}