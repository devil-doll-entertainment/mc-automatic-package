package com.sxnnyside.autopack.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sxnnyside.autopack.AutomaticPackageCommon;
import com.sxnnyside.autopack.platform.Services;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Platform-independent configuration stored in the game's config directory.
 * File location: {@code .minecraft/config/automaticpackage.json}
 */
public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static ModConfig instance;

    // ---- Configurable fields ----

    /**
     * Target resource pack ID to toggle (e.g. "file/my_pack.zip").
     * Empty string by default so the user is guided to pick a pack.
     */
    private String targetPackId = "";

    /**
     * Cached human-readable display name for the target pack.
     */
    private String targetPackDisplayName = "";

    /**
     * Whether to play an audible UI click when toggling.
     */
    private boolean playToggleSound = true;

    /**
     * Whether to show feedback in the action bar overlay (true) or chat (false).
     */
    private boolean notifyActionBar = true;

    // ---- Getters and Setters ----

    public String getTargetPackId() {
        return targetPackId != null ? targetPackId : "";
    }

    public void setTargetPackId(String targetPackId) {
        this.targetPackId = targetPackId != null ? targetPackId : "";
        save();
    }

    public String getTargetPackDisplayName() {
        return targetPackDisplayName != null ? targetPackDisplayName : "";
    }

    public void setTargetPackDisplayName(String displayName) {
        this.targetPackDisplayName = displayName != null ? displayName : "";
        save();
    }

    public void setTargetPack(String packId, String displayName) {
        this.targetPackId = packId != null ? packId : "";
        this.targetPackDisplayName = displayName != null ? displayName : "";
        save();
    }

    public boolean isPlayToggleSound() {
        return playToggleSound;
    }

    public void setPlayToggleSound(boolean playToggleSound) {
        this.playToggleSound = playToggleSound;
        save();
    }

    public boolean isNotifyActionBar() {
        return notifyActionBar;
    }

    public void setNotifyActionBar(boolean notifyActionBar) {
        this.notifyActionBar = notifyActionBar;
        save();
    }

    public boolean hasTargetPack() {
        return targetPackId != null && !targetPackId.trim().isEmpty();
    }

    // ---- Lifecycle ----

    private static Path getConfigPath() {
        return Services.PLATFORM.getConfigDirectory().resolve("automaticpackage.json");
    }

    public static void load() {
        Path path = getConfigPath();
        if (Files.exists(path)) {
            try (Reader reader = Files.newBufferedReader(path)) {
                instance = GSON.fromJson(reader, ModConfig.class);
                if (instance == null) {
                    instance = new ModConfig();
                }
                AutomaticPackageCommon.LOGGER.info("Configuration loaded: targetPackId='{}'", instance.targetPackId);
            } catch (Exception e) {
                AutomaticPackageCommon.LOGGER.error("Failed to read configuration; using defaults.", e);
                instance = new ModConfig();
            }
        } else {
            instance = new ModConfig();
            AutomaticPackageCommon.LOGGER.info("No configuration found; creating default at {}", path);
        }
        save();
    }

    public static void save() {
        Path path = getConfigPath();
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(get(), writer);
            }
        } catch (IOException e) {
            AutomaticPackageCommon.LOGGER.error("Failed to save configuration.", e);
        }
    }

    public static ModConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }
}
