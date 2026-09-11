package com.sxnnyside.autopack.platform;

import java.nio.file.Path;

/**
 * Cross-loader abstraction for environment and platform services.
 */
public interface PlatformHelper {

    /**
     * Gets the path to the game's config directory.
     */
    Path getConfigDirectory();

    /**
     * Gets the name of the current platform ("Fabric" or "NeoForge").
     */
    String getPlatformName();

    /**
     * Checks if a given mod ID is loaded in the current environment.
     */
    boolean isModLoaded(String modId);
}
