package amedonai.ss_celestial_threat;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import amedonai.ss_celestial_threat.screen.SkyStoneFurnaceScreenHandler;

public class ModScreenHandlers {
    public static final ScreenHandlerType<SkyStoneFurnaceScreenHandler> SKY_STONE_FURNACE =
            Registry.register(
                    Registries.SCREEN_HANDLER,
                    Identifier.of("ss_celestial_threat", "sky_stone_furnace"),
                    new ScreenHandlerType<>(SkyStoneFurnaceScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
            );

    public static void registerAllScreenHandlers() {
    }
}