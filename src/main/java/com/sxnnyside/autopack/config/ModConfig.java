package com.sxnnyside.autopack.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sxnnyside.autopack.AutomaticPackageMod;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages the mod configuration stored as a JSON file in the Fabric config directory.
 *
 * <p>Location: {@code .minecraft/config/automaticpackage.json}</p>
 *
 * <p>The file is created automatically on first launch with sensible defaults.
 * Users can edit it while Minecraft is closed; changes take effect on next launch.</p>
 */
public class ModConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("automaticpackage.json");

    private static ModConfig instance;

    // ---- Configurable fields ----

    /**
     * The resource pack ID to toggle.
     *
     * <p>For packs in the {@code resourcepacks} folder the ID is {@code file/<filename>}.
     * Example: a file named {@code xray.zip} has the ID {@code file/xray.zip}.</p>
     */
    private String resourcePackId = "file/xray.zip";

    // ---- Accessors ----

    public String getResourcePackId() {
        return resourcePackId;
    }

    public void setResourcePackId(String resourcePackId) {
        this.resourcePackId = resourcePackId;
        save();
    }

    // ---- Lifecycle ----

    /**
     * Load the configuration from disk, or create a new default config if none exists.
     */
    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {
                instance = GSON.fromJson(reader, ModConfig.class);
                if (instance == null) {
                    instance = new ModConfig();
                }
                AutomaticPackageMod.LOGGER.info("[MC-AutomaticPackage] Config loaded: packId='{}'",
                        instance.resourcePackId);
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                AutomaticPackageMod.LOGGER.error("[MC-AutomaticPackage] Failed to read config; using defaults.", e);
                instance = new ModConfig();
            }
        } else {
            instance = new ModConfig();
            AutomaticPackageMod.LOGGER.info("[MC-AutomaticPackage] No config found; creating default at {}",
                    CONFIG_PATH);
        }
        save(); // always write back so the file exists and is up to date
    }

    /**
     * Persist the current configuration to disk.
     */
    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(instance, writer);
            }
        } catch (IOException e) {
            AutomaticPackageMod.LOGGER.error("[MC-AutomaticPackage] Failed to save config.", e);
        }
    }

    /**
     * Returns the singleton config instance. Loads from disk if not yet initialized.
     */
    public static ModConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }
}
