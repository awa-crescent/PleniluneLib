package lib.plenilune;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class PleniluneLib implements ModInitializer {
	public static final String MOD_ID = "plenilune-lib";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("PleniluneLib loaded.");
	}
}