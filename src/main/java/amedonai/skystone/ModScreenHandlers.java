package amedonai.skystone;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import amedonai.skystone.screen.SkyStoneFurnaceScreenHandler;

public class ModScreenHandlers {
    public static ScreenHandlerType<SkyStoneFurnaceScreenHandler> SKY_STONE_FURNACE;

    public static void registerAllScreenHandlers() {
        SKY_STONE_FURNACE = ScreenHandlerRegistry.registerSimple(
                new Identifier(SkyStone.MOD_ID, "sky_stone_furnace"),
                SkyStoneFurnaceScreenHandler::new
        );
    }
}