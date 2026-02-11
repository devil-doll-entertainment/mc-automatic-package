package com.sxnnyside.autopack.pack;

import com.sxnnyside.autopack.AutomaticPackageMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Programmatically toggles a resource pack on or off and triggers a resource reload.
 *
 * <p>This class operates entirely on the client side: it manipulates the
 * {@link ResourcePackManager}, persists the change to {@code options.txt},
 * and forces a full resource reload so the change takes effect immediately.</p>
 */
public final class ResourcePackToggle {

    /** Result of a toggle attempt. */
    public enum ToggleResult {
        /** The pack was found and is now enabled. */
        ENABLED,
        /** The pack was found and is now disabled. */
        DISABLED,
        /** The pack ID was not found among available resource packs. */
        NOT_FOUND
    }

    private ResourcePackToggle() { }

    /**
     * Toggle a resource pack by its ID.
     *
     * @param client the Minecraft client instance
     * @param packId resource pack identifier (e.g. {@code "file/xray.zip"})
     * @return the result of the toggle operation
     */
    public static ToggleResult toggle(MinecraftClient client, String packId) {
        ResourcePackManager manager = client.getResourcePackManager();

        // Refresh the list of available packs (picks up newly added zips)
        manager.scanPacks();

        // Check whether the target pack exists
        if (!isPackAvailable(manager, packId)) {
            logAvailablePacks(manager);
            return ToggleResult.NOT_FOUND;
        }

        // Determine current enabled set
        List<String> enabledIds = manager.getEnabledProfiles().stream()
                .map(ResourcePackProfile::getId)
                .collect(Collectors.toCollection(ArrayList::new));

        boolean wasEnabled = enabledIds.contains(packId);

        if (wasEnabled) {
            enabledIds.remove(packId);
        } else {
            enabledIds.add(packId);
        }

        // Apply the new enabled set
        manager.setEnabledProfiles(enabledIds);

        // Persist the change so it survives a restart
        syncOptionsFromManager(client, manager);

        // Force a full resource reload
        client.reloadResources();

        return wasEnabled ? ToggleResult.DISABLED : ToggleResult.ENABLED;
    }

    /**
     * Check whether a pack is currently enabled.
     *
     * @param client the Minecraft client instance
     * @param packId resource pack identifier
     * @return {@code true} if the pack is in the enabled set
     */
    public static boolean isPackEnabled(MinecraftClient client, String packId) {
        return client.getResourcePackManager().getEnabledProfiles().stream()
                .anyMatch(profile -> profile.getId().equals(packId));
    }

    // ---- internal helpers ----

    private static boolean isPackAvailable(ResourcePackManager manager, String packId) {
        for (ResourcePackProfile profile : manager.getProfiles()) {
            if (profile.getId().equals(packId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Write the current enabled-pack list back into {@code GameOptions}
     * so that {@code options.txt} stays in sync and the packs persist across restarts.
     */
    private static void syncOptionsFromManager(MinecraftClient client, ResourcePackManager manager) {
        List<String> userSelectedPacks = new ArrayList<>();
        Collection<ResourcePackProfile> enabled = manager.getEnabledProfiles();

        for (ResourcePackProfile profile : enabled) {
            // Skip packs that Minecraft always keeps enabled (e.g. "vanilla", "fabric")
             if (profile.getSource().canBeEnabledLater()) {
                continue;
            }
            userSelectedPacks.add(profile.getId());
        }

        client.options.resourcePacks.clear();
        client.options.resourcePacks.addAll(userSelectedPacks);
        client.options.write();
    }

    private static void logAvailablePacks(ResourcePackManager manager) {
        AutomaticPackageMod.LOGGER.warn("Available packs:");
        for (ResourcePackProfile profile : manager.getProfiles()) {
            AutomaticPackageMod.LOGGER.warn("  - id='{}' name='{}'",
                    profile.getId(), profile.getDisplayName().getString());
        }
    }
}
