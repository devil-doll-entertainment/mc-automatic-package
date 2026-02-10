package com.sxnnyside.autopack;

import com.sxnnyside.autopack.config.ModConfig;
import com.sxnnyside.autopack.keybind.KeybindHandler;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MC-AutomaticPackage — toggle any resource pack with a keybind.
 *
 * <p>Client-side Fabric mod. Entry point registered in {@code fabric.mod.json}
 * under the {@code client} entrypoint.</p>
 */
public class AutomaticPackageMod implements ClientModInitializer {

    public static final String MOD_ID = "automaticpackage";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[MC-AutomaticPackage] Initializing...");
        ModConfig.load();
        KeybindHandler.register();
        LOGGER.info("[MC-AutomaticPackage] Ready. Toggle key bound.");
    }
}
