package com.sxnnyside.autopack.pack;

import com.sxnnyside.autopack.AutomaticPackageCommon;
import com.sxnnyside.autopack.config.ModConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Controller for scanning, querying, and toggling client resource packs.
 */
public final class ResourcePackController {

    public enum ToggleResult {
        ENABLED,
        DISABLED,
        NOT_FOUND,
        NO_PACK_SELECTED,
        COOLDOWN_ACTIVE
    }

    private static long lastToggleTimestamp = 0;

    private ResourcePackController() {}

    /**
     * Scans and returns all available user-configurable resource packs.
     */
    public static @NotNull List<PackInfo> getAvailableUserPacks(@NotNull Minecraft client) {
        PackRepository repository = client.getResourcePackRepository();
        repository.reload();

        Collection<Pack> available = repository.getAvailablePacks();
        Collection<Pack> selected = repository.getSelectedPacks();
        String currentTargetId = ModConfig.get().getTargetPackId();

        List<PackInfo> list = new ArrayList<>();
        for (Pack pack : available) {
            boolean isRequired = pack.isRequired();
            boolean isEnabled = selected.contains(pack);
            boolean isTarget = pack.getId().equals(currentTargetId);

            list.add(new PackInfo(
                    pack.getId(), pack.getTitle(), pack.getDescription(), isEnabled, isTarget, isRequired));
        }

        return list;
    }

    /**
     * Toggles the currently configured quick-toggle pack.
     */
    public static @NotNull ToggleResult toggleConfiguredPack(@NotNull Minecraft client) {
        ModConfig config = ModConfig.get();
        if (!config.hasTargetPack()) {
            return ToggleResult.NO_PACK_SELECTED;
        }

        return togglePack(client, config.getTargetPackId());
    }

    /**
     * Toggles any pack by its ID with anti-spam reload debouncing.
     */
    public static @NotNull ToggleResult togglePack(@NotNull Minecraft client, @Nullable String packId) {
        if (packId == null || packId.trim().isEmpty()) {
            return ToggleResult.NO_PACK_SELECTED;
        }

        long now = System.currentTimeMillis();
        long cooldown = ModConfig.get().getReloadCooldownMs();
        if (now - lastToggleTimestamp < cooldown) {
            AutomaticPackageCommon.LOGGER.debug("Resource reload blocked: cooldown active.");
            return ToggleResult.COOLDOWN_ACTIVE;
        }

        PackRepository repository = client.getResourcePackRepository();
        repository.reload();

        Optional<Pack> targetPackOpt = repository.getAvailablePacks().stream()
                .filter(p -> p.getId().equals(packId))
                .findFirst();

        if (targetPackOpt.isEmpty()) {
            AutomaticPackageCommon.LOGGER.warn("Resource pack '{}' not found.", packId);
            return ToggleResult.NOT_FOUND;
        }

        // Current selected list
        List<String> currentSelected = new ArrayList<>(repository.getSelectedIds());
        boolean wasEnabled = currentSelected.contains(packId);

        if (wasEnabled) {
            currentSelected.remove(packId);
        } else {
            // Stack Priority: Add to end so toggled utility pack takes highest precedence over other textures
            currentSelected.remove(packId);
            currentSelected.add(packId);
        }

        // Apply new set of selected packs
        repository.setSelected(currentSelected);

        // Sync with options.txt so changes survive client restart
        syncOptions(client, repository);

        // Record cooldown timestamp before triggering expensive reload
        lastToggleTimestamp = System.currentTimeMillis();

        // Audio feedback delegation
        ResourcePackFeedback.playToggleSound(client, wasEnabled);

        // Trigger full resource reload
        client.reloadResourcePacks();

        return wasEnabled ? ToggleResult.DISABLED : ToggleResult.ENABLED;
    }

    /**
     * Resolves human-friendly display name of a pack.
     */
    public static @NotNull Component getPackDisplayName(@NotNull Minecraft client, @Nullable String packId) {
        if (packId == null || packId.trim().isEmpty()) {
            return Component.empty();
        }

        PackRepository repository = client.getResourcePackRepository();
        Pack pack = repository.getPack(packId);
        if (pack != null) {
            return pack.getTitle();
        }

        String cached = ModConfig.get().getTargetPackDisplayName();
        if (cached != null && !cached.isEmpty()) {
            return Component.literal(cached);
        }

        return Component.literal(packId);
    }

    /**
     * Sends formatted feedback to the player (action bar or chat).
     */
    public static void sendFeedback(@NotNull Minecraft client, @NotNull Component message) {
        ResourcePackFeedback.sendFeedback(client, message);
    }

    /**
     * Formats feedback message for toggle results.
     */
    public static @NotNull Component formatResult(
            @NotNull Minecraft client, @NotNull ToggleResult result, @Nullable String packId) {
        return ResourcePackFeedback.formatResult(client, result, packId);
    }

    private static void syncOptions(Minecraft client, PackRepository repository) {
        List<String> userSelected = new ArrayList<>();
        for (Pack pack : repository.getSelectedPacks()) {
            // Keep packs that are user-configurable (skip required/core packs)
            if (!pack.isRequired()) {
                userSelected.add(pack.getId());
            }
        }

        client.options.resourcePacks.clear();
        client.options.resourcePacks.addAll(userSelected);
        client.options.save();
    }
}
