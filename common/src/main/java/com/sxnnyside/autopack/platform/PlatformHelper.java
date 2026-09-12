package com.sxnnyside.autopack.platform;

import java.nio.file.Path;
import org.jetbrains.annotations.NotNull;

/**
 * Cross-loader abstraction for environment and platform services.
 */
public interface PlatformHelper {

    /**
     * Gets the path to the game's config directory.
     */
    @NotNull
    Path getConfigDirectory();

    /**
     * Gets the name of the current platform ("Fabric" or "NeoForge").
     */
    @NotNull
    String getPlatformName();

    /**
     * Checks if a given mod ID is loaded in the current environment.
     */
    boolean isModLoaded(@NotNull String modId);
}
