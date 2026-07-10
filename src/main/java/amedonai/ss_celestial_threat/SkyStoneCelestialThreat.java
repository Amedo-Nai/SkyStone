package amedonai.ss_celestial_threat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyStoneCelestialThreat implements ModInitializer {
	public static final String MOD_ID = "skystone-celestial-threat"; // Твой ID из fabric.mod.json
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading base content for SkyStone: Celestial Threat...");

	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}